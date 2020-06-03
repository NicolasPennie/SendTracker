package sendservice;

import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final Gson gson = new Gson();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input,  final Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log(gson.toJson(input));

        switch (input.getHttpMethod()) {
            case "GET":
                return getAllSends();

            default:
                return handleUnexpectedMethod();
        }
    }

    private APIGatewayProxyResponseEvent getAllSends() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        response.setBody("{ \"message\": \"Welcome to SendTracker!\" }");
        response.setStatusCode(200);
        response.setIsBase64Encoded(false);
        return response;
    }

    private APIGatewayProxyResponseEvent handleUnexpectedMethod() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(400);
        response.setIsBase64Encoded(false);
        return response;
    }
}
