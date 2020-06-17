package sendservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

public class AppTest {
  private Gson gson = new Gson();

  @Test
  public void getRequestSuccess() throws SQLException {
    // arrange
    var sendProvider = mock(SendProvider.class);
    var mockSend = new Send("1", "name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    when(sendProvider.getAllSends()).thenReturn(List.of(mockSend));
    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();
    List<Send> parsedSends = gson.fromJson(content, new TypeToken<List<Send>>() {}.getType());

    // assert
    assertEquals((int) result.getStatusCode(), 200);
    assertEquals(parsedSends.size(), 1);
    assertEquals(gson.toJson(parsedSends.get(0)), gson.toJson(mockSend));
  }

  @Test
  public void getRequestFailure() throws SQLException {
    // arrange
    var sendProvider = mock(SendProvider.class);
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
  public void unexpectedRequest() {
    // arrange
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
