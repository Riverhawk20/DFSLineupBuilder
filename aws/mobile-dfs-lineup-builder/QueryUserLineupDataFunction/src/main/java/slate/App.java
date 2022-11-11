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
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private String DYNAMODB_TABLE_NAME_USER = "dfs-lineup-builder-user";
    private String DYNAMODB_TABLE_NAME_LINEUPS = "dfs-lineup-builder-lineup";
    private String LINEUPS_GSI = "gsi-userid";
    private Regions REGION = Regions.US_EAST_1;

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        Gson gson = new Gson();
        try {
            Map<String, String> inputParams = input.getQueryStringParameters();
            if (inputParams.containsKey("UserId")) {
                String userId = inputParams.get("UserId");
                AmazonDynamoDBClient clientShell = new AmazonDynamoDBClient();
                clientShell.setRegion(Region.getRegion(REGION));
                DynamoDB dynamoDB = new DynamoDB(clientShell);

                Table lineupTable = dynamoDB.getTable(DYNAMODB_TABLE_NAME_LINEUPS);
                Index lineupUserIdIndex = lineupTable.getIndex(LINEUPS_GSI);
                QuerySpec lineupSpec = new QuerySpec()
                        .withKeyConditionExpression("UserId  = :v_id")
                        .withValueMap(new ValueMap()
                                .withString(":v_id", userId));

                ItemCollection<QueryOutcome> lineupItems = lineupUserIdIndex.query(lineupSpec);
                Iterator<Item> lineupIter = lineupItems.iterator();
                List<Lineup> lineupsList = new ArrayList<Lineup>();
                while (lineupIter.hasNext()) {
                    lineupsList.add(gson.fromJson(lineupIter.next().toJSON(),Lineup.class));
                }

                Table userTable = dynamoDB.getTable(DYNAMODB_TABLE_NAME_USER);
                QuerySpec userSpec = new QuerySpec()
                        .withKeyConditionExpression("UserId  = :v_id")
                        .withValueMap(new ValueMap()
                                .withString(":v_id", userId));
                ItemCollection<QueryOutcome> userItems = userTable.query(userSpec);
                Iterator<Item> userIter = userItems.iterator();
                List<User> userList = new ArrayList<User>();
                while (userIter.hasNext()) {
                    userList.add(gson.fromJson(userIter.next().toJSON(),User.class));
                }

                UserLineupResponse res = new UserLineupResponse(userList.get(0), lineupsList);
                // convert DynamoDB result into JSON!
                return response
                        .withStatusCode(200)
                        .withBody(new Gson().toJson(res));
            } else {
                throw new Exception("No UserID Passed in");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }

    public List<Object> getJson(List<Map<String, AttributeValue>> mapList) {
        List<Object> finalJson = new ArrayList();
        for (Map<String, AttributeValue> eachEntry : mapList) {
            finalJson.add(mapToJson(eachEntry));
        }
        return finalJson;
    }

    public Map<String, Object> mapToJson(Map<String, AttributeValue> keyValueMap) {
        Map<String, Object> finalKeyValueMap = new HashMap();
        for (Map.Entry<String, AttributeValue> entry : keyValueMap.entrySet()) {
            if (entry.getValue().getL() != null) {
                // the attribute is a list of attribute values that are maps
                List<Map<String, AttributeValue>> mapList = new ArrayList<Map<String, AttributeValue>>();
                for (AttributeValue attr : entry.getValue().getL()) {
                    mapList.add(attr.getM());
                }
                // get the list of maps and convert to json
                finalKeyValueMap.put(entry.getKey(), getJson(mapList));
            } else if (entry.getValue().getS() != null) {
                finalKeyValueMap.put(entry.getKey(), entry.getValue().getS());
            } else if (entry.getValue().getN() != null) {
                finalKeyValueMap.put(entry.getKey(), entry.getValue().getN());
            } else if (entry.getValue().getBOOL() != null) {
                finalKeyValueMap.put(entry.getKey(), entry.getValue().getBOOL());
            } else {
                finalKeyValueMap.put(entry.getKey(), mapToJson(entry.getValue().getM()));
            }
        }
        return finalKeyValueMap;
    }

}
