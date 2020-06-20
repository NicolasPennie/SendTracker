package sendservice;

import java.sql.*;
import java.util.HashMap;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import sendservice.models.Send;
import sendservice.providers.PostgresSendProvider;
import sendservice.providers.SendProvider;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final Gson gson = new Gson();
    private LambdaLogger logger;
    private SendProvider sendProvider;

    public App() {}

    public App(SendProvider sendProvider) {
        this.sendProvider = sendProvider;
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        logger = context.getLogger();

        if (sendProvider == null) {
            try {
                sendProvider = new PostgresSendProvider(logger);
            }
            catch (ClassNotFoundException e) {
                logger.log("JDBC PostgreSQL driver was not found: " + e.getMessage());
                return handleInternalError();
            }
        }

        switch (input.getHttpMethod()) {
            case "GET":
                return getAllSends();

            case "POST":
                return addSend(input);

            default:
                return handleBadRequest();
        }
    }

    private APIGatewayProxyResponseEvent getAllSends() {
        try {
            var sends = sendProvider.getAllSends();
            var json = gson.toJson(sends);
            return handleJsonSuccess(json);
        }
        catch (SQLException e) {
            logger.log("Failed to get all sends. Database error occurred: " + e.getMessage());
            return handleInternalError();
        }
        catch (Exception e) {
            logger.log("Failed to get all sends. Something unexpected occurred: " + e.getMessage());
            return handleInternalError();
        }
    }

    private APIGatewayProxyResponseEvent addSend(final APIGatewayProxyRequestEvent input) {
        try {
            var send = gson.fromJson(input.getBody(), Send.class);
            var id = sendProvider.addSend(send);
            var json = String.format("{\"id\": %s }", id);
            return handleJsonSuccess(json);
        }
        catch (SQLException e) {
            logger.log("Failed to add send. Database error occurred: " + e.getMessage());
            return handleInternalError();
        }
        catch (Exception e) {
            logger.log("Failed to get add send. Something unexpected occurred: " + e.getMessage());
            return handleInternalError();
        }
    }

    private APIGatewayProxyResponseEvent handleJsonSuccess(String json) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        response.setBody(json);
        response.setStatusCode(200);
        return response;
    }

    private APIGatewayProxyResponseEvent handleInternalError() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(500);
        response.setIsBase64Encoded(false);
        return response;
    }

    private APIGatewayProxyResponseEvent handleBadRequest() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(400);
        response.setIsBase64Encoded(false);
        return response;
    }
}
