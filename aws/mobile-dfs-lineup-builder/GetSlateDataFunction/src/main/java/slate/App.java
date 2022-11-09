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

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private String DYNAMODB_TABLE_NAME = "dfs-lineup-builder-slate";
    private Regions REGION = Regions.US_EAST_1;

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            ArrayList<Slate> slates = getSlateData();
            ArrayList<Item> writeItems = new ArrayList<Item>();
            for (Slate slate : slates) {
                writeItems.add(new Item().withJSON("document", new Gson().toJson(slate)).withPrimaryKey("SlateId",
                        slate.SlateId, "StartDate", slate.StartDate).with("Sport", "NFL"));
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

    public ArrayList<Slate> getSlateData() {
        // query DraftKings contests which returns the high level data of all draftable
        // groups (slates)
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("https://www.draftkings.com/lobby/getcontests?sport=NFL"))
                .header("accept", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            ContestsRes contestRes = new Gson().fromJson(response.body(), ContestsRes.class);
            // remove draft groups we do not want (ex: Madden draft groups)
            contestRes.DraftGroups.removeIf(DraftGroup.DraftGroupFilterOut);
            ArrayList<DraftGroup> draftGroups = contestRes.DraftGroups;
            // for each draftable group query the remaining data we need and build a Slate
            // object
            ArrayList<Slate> slates = new ArrayList<Slate>();
            for (DraftGroup draftGroup : draftGroups) {
                HttpRequest draftGroupRequest = HttpRequest.newBuilder(
                        URI.create(String.format("https://api.draftkings.com/draftgroups/v1/draftgroups/%s/draftables",
                                draftGroup.DraftGroupId)))
                        .header("accept", "application/json")
                        .GET()
                        .build();
                try {
                    HttpResponse<String> draftGroupResponse = client.send(draftGroupRequest, BodyHandlers.ofString());
                    DraftGroupRes draftGroupRes = new Gson().fromJson(draftGroupResponse.body(), DraftGroupRes.class);
                    ArrayList<Player> players = new ArrayList<Player>();
                    // create player object for each draftable
                    // rosterSlotId !== 70
                    for (Draftable draftable : draftGroupRes.draftables) {
                        // filter out duplicated players (UTIL Spot)
                        if (draftable.rosterSlotId != 70) {
                            // get the competition that pertains to this player
                            Competition playerCompetiton = draftGroupRes.competitions.stream()
                                    .filter(c -> c.homeTeam.abbreviation.equals(draftable.teamAbbreviation)
                                            || c.awayTeam.abbreviation.equals(draftable.teamAbbreviation))
                                    .findAny().orElse(null);
                            if (playerCompetiton != null) {
                                // get the opponent team name
                                String opponent = playerCompetiton.homeTeam.abbreviation
                                        .equals(draftable.teamAbbreviation)
                                                ? playerCompetiton.awayTeam.abbreviation
                                                : playerCompetiton.homeTeam.abbreviation;

                                players.add(
                                        new Player(draftable.draftableId, draftable.displayName, draftable.position,
                                                draftable.teamAbbreviation,
                                                opponent, draftable.salary, draftGroup.DraftGroupId));
                            }
                        }

                    }
                    if (players.size() > 0) {
                        // build slate object and add to list
                        slates.add(new Slate(draftGroup.DraftGroupId, draftGroup.ContestStartTimeSuffix,
                                draftGroup.StartDate, players));
                    }
                } catch (Exception e) {
                    System.out.println("Failed to get Draft Group Data");
                    e.printStackTrace();
                    return null;
                }
            }
            return slates;
        } catch (Exception e) {
            System.out.println("Failed to get DK Data");
            e.printStackTrace();
            return null;
        }
    }
}
