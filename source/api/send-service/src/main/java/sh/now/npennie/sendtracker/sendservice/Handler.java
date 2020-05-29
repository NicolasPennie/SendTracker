package sh.now.npennie.sendtracker.sendservice;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class Handler implements RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent>{
  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public APIGatewayV2ProxyResponseEvent handleRequest(APIGatewayV2ProxyRequestEvent event, Context context)
  {
    // Log Context
    LambdaLogger logger = context.getLogger();
    logger.log("Event: " + gson.toJson(event));

    // Form headers
    HashMap<String, String> headers = new HashMap<String, String>();
    headers.put("Content-Type", "text/html");

    // Generate response
    APIGatewayV2ProxyResponseEvent response = new APIGatewayV2ProxyResponseEvent();
    response.setHeaders(headers);
    response.setStatusCode(200);
    response.setIsBase64Encoded(false);
    response.setBody("<!DOCTYPE html><html><head><title>AWS Lambda sample</title></head><body>" +
            "<h1>Welcome to SendAPI</h1>" +
            "</body></html>");

    return response;
  }
}