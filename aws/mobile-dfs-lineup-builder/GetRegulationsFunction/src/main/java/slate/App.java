package slate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import slate.Regulations;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private String DYNAMODB_TABLE_NAME = "dfs-lineup-builder-regulations";
    private Regions REGION = Regions.US_EAST_1;

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            ArrayList<Regulations> regulations = getRegulationData();
            // split states into two lists since a batch write is limited to 25 items
            // (51 Regulations with DC)
            ArrayList<Item> writeItemsFirst25 = new ArrayList<Item>();
            ArrayList<Item> writeItemsMiddle25 = new ArrayList<Item>();
            ArrayList<Item> writeItemsLast25 = new ArrayList<Item>();
            for (int i = 0; i < regulations.size(); i++) {
                Regulations regulation = regulations.get(i);
                Item item = new Item()
                        .withPrimaryKey("StateName", regulation.StateName)
                        .withBoolean("IsLegal", regulation.IsLegal);
                if (i < 25) {
                    writeItemsFirst25.add(item);
                } else if (i < 50) {
                    writeItemsMiddle25.add(item);
                } else {
                    writeItemsLast25.add(item);
                }
            }

            // write items to DynamoDB
            AmazonDynamoDBClient clientShell = new AmazonDynamoDBClient();
            clientShell.setRegion(Region.getRegion(REGION));
            DynamoDB dbClient = new DynamoDB(clientShell);
            writeRegulationDataToDynamoDB(writeItemsFirst25, dbClient);
            writeRegulationDataToDynamoDB(writeItemsMiddle25, dbClient);
            writeRegulationDataToDynamoDB(writeItemsLast25, dbClient);
            return response
                    .withStatusCode(200)
                    .withBody("Success!");
        } catch (Exception e) {
            e.printStackTrace();
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

    public void writeRegulationDataToDynamoDB(ArrayList<Item> writeItems, DynamoDB dbClient) {
        TableWriteItems tableWriteItems = new TableWriteItems(DYNAMODB_TABLE_NAME).withItemsToPut(writeItems);
        BatchWriteItemOutcome outcome = dbClient.batchWriteItem(tableWriteItems);
        do {
            // Check for unprocessed keys which could happen if you exceed
            // provisioned throughput

            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

            if (outcome.getUnprocessedItems().size() == 0) {
                System.out.println("No unprocessed items found");
            } else {
                System.out.println("Retrieving the unprocessed items");
                outcome = dbClient.batchWriteItemUnprocessed(unprocessedItems);
            }

        } while (outcome.getUnprocessedItems().size() > 0);
    }

    public ArrayList<Regulations> getRegulationData() {
        try {
            // get html from action network (a well maintained up to date page)
            Document doc = Jsoup.connect(
                    "https://www.actionnetwork.com/legal-online-sports-betting/where-is-daily-fantasy-sports-legal")
                    .get();
            ArrayList<Regulations> regulations = new ArrayList<Regulations>();
            Character checkMark = Character.toChars(10003)[0];
            // get DraftKings regulations for DFS for each slate by scraping the table
            Elements regulationsElements = doc.select(".article-view__table-container tbody tr");
            for (Element regulationRow : regulationsElements) {
                String state = regulationRow.select("td:nth-child(1)").text();
                String isLegalText = regulationRow.select("td:nth-child(2)").text();
                boolean isLegal = isLegalText.charAt(0) == checkMark;
                if (state != null && state.length() > 0) {
                    regulations.add(new Regulations(state, isLegal));
                }
            }
            return regulations;
        } catch (Exception e) {
            System.out.println("Failed to get Regulations Data");
            e.printStackTrace();
            return null;
        }
    }
}
