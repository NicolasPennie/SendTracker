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

            case "PUT":
                return editSend(input);

            case "DELETE":
                return deleteSend(input);

            default:
                return handleBadRequest();
        }
    }

    private APIGatewayProxyResponseEvent getAllSends() {
        try {
            var sends = sendProvider.getAllSends();
            var json = gson.toJson(sends);
            return handleJsonSuccess(json, 200);
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
            return handleJsonSuccess(json, 201);
        }
        catch (SQLException e) {
            logger.log("Failed to add send. Database error occurred: " + e.getMessage());
            return handleInternalError();
        }
        catch (Exception e) {
            logger.log("Failed to add send. Something unexpected occurred: " + e.getMessage());
            return handleInternalError();
        }
    }

    private APIGatewayProxyResponseEvent editSend(final APIGatewayProxyRequestEvent input) {
        int id;
        Send send;

        try {
            // Path is formatted as "/send/{id}"
            var pathSegments = input.getPath().split("/");
            id = Integer.parseInt(pathSegments[2]);
            send = gson.fromJson(input.getBody(), Send.class);
        }
        catch (Exception e) {
            logger.log("Failed to edit send. Invalid input was provided: " + e.getMessage());
            return handleBadRequest();
        }

        try {
            sendProvider.editSend(id, send);
            return handleSuccess(204);
        }
        catch (SQLException e) {
            logger.log("Failed to edit send. Database error occurred: " + e.getMessage());
            return handleInternalError();
        }
        catch (Exception e) {
            logger.log("Failed to edit send. Something unexpected occurred: " + e.getMessage());
            return handleInternalError();
        }
    }

    private APIGatewayProxyResponseEvent deleteSend(final APIGatewayProxyRequestEvent input) {
        int id;
        try {
            // Path is formatted as "/send/{id}"
            var pathSegments = input.getPath().split("/");
            id = Integer.parseInt(pathSegments[2]);
        }
        catch (Exception e) {
            logger.log("Failed to delete send. Invalid input was provided: " + e.getMessage());
            return handleBadRequest();
        }

        try {
            sendProvider.deleteSend(id);
            return handleSuccess(204);
        }
        catch (SQLException e) {
            logger.log("Failed to delete send. Database error occurred: " + e.getMessage());
            return handleInternalError();
        }
        catch (Exception e) {
            logger.log("Failed to delete send. Something unexpected occurred: " + e.getMessage());
            return handleInternalError();
        }
    }

    private APIGatewayProxyResponseEvent handleSuccess(int statusCode) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        response.setStatusCode(statusCode);
        return response;
    }

    private APIGatewayProxyResponseEvent handleJsonSuccess(String json, int status) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setIsBase64Encoded(false);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        response.setHeaders(headers);
        response.setBody(json);
        response.setStatusCode(status);
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
