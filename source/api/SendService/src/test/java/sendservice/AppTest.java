package sendservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import sendservice.models.Send;
import sendservice.models.Style;
import sendservice.models.TickType;
import sendservice.providers.PostgresSendProvider;
import sendservice.providers.SendProvider;

import java.sql.SQLException;
import java.util.List;

public class AppTest {
  private Gson gson = new Gson();

  @Test
  public void getRequestSuccess() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockSend = new Send(1, "name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    when(sendProvider.getAllSends()).thenReturn(List.of(mockSend));
    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals((int) result.getStatusCode(), 200);
    assertNotNull(content);
  }

  @Test
  public void getRequestFailure() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    when(sendProvider.getAllSends()).thenThrow(new SQLException());
    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals((int) result.getStatusCode(), 500);
    assertNull(content);
  }

  @Test
  public void postRequestSuccess() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockGeneratedId = 1;
    when(sendProvider.addSend(any())).thenReturn(mockGeneratedId);


    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("POST");
    var mockSend = new Send(-1, "name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    var mockBody = gson.toJson(mockSend);
    event.setBody(mockBody);
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals((int) result.getStatusCode(), 200);
    assert(content.contains(Integer.toString(mockGeneratedId)));
  }

  @Test
  public void postRequestFailure() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockGeneratedId = 1;
    when(sendProvider.addSend(any())).thenThrow(new SQLException());

    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("POST");
    var mockSend = new Send(-1, "name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    var mockBody = gson.toJson(mockSend);
    event.setBody(mockBody);
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);

    // assert
    assertEquals((int) result.getStatusCode(), 500);
  }

  @Test
  public void unexpectedRequest() {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    App app = new App();
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("UNEXPECTED");
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals((int) result.getStatusCode(), 400);
    assertNull(content);
  }
}
