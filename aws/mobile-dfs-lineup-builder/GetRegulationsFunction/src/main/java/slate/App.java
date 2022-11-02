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
            ArrayList<Item> writeItems = new ArrayList<Item>();
            for (Regulations regulation : regulations) {
                System.out.println(regulation.IsLegal + " " + regulation.StateName);
                writeItems.add(new Item()
                        .withPrimaryKey("StateName", regulation.StateName)
                        .withBoolean("IsLegal", regulation.IsLegal));
            }
            TableWriteItems tableWriteItems = new TableWriteItems(DYNAMODB_TABLE_NAME).withItemsToPut(writeItems);
            AmazonDynamoDBClient clientShell = new AmazonDynamoDBClient();
            clientShell.setRegion(Region.getRegion(REGION));
            DynamoDB dbClient = new DynamoDB(clientShell);
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

    public ArrayList<Regulations> getRegulationData() {
        try {
            Document doc = Jsoup.connect(
                    "https://www.actionnetwork.com/legal-online-sports-betting/where-is-daily-fantasy-sports-legal")
                    .get();
            ArrayList<Regulations> regulations = new ArrayList<Regulations>();
            Elements regulationsElements = doc.select(".article-view__table-container tr");
            for (Element regulationRow : regulationsElements) {
                String state = regulationRow.select("td:nth-child(1)").text();
                boolean isLegal = regulationRow.select("td:nth-child(2)").text() == "✓";
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
