package sendservice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import org.junit.Test;
import sendservice.models.Send;
import sendservice.models.Style;
import sendservice.models.TickType;
import sendservice.providers.PostgresSendProvider;
import sendservice.providers.SendProvider;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AppTest {
  private Gson gson = new Gson();

  @Test
  public void getRequestSuccess() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockSend = new Send(UUID.randomUUID(), "name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    when(sendProvider.getAllSends()).thenReturn(List.of(mockSend));
    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals(200, (int) result.getStatusCode());
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
    assertEquals(500, (int) result.getStatusCode());
    assertNull(content);
  }

  @Test
  public void postRequestSuccess() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockGeneratedId = UUID.randomUUID();
    when(sendProvider.addSend(any())).thenReturn(mockGeneratedId);

    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("POST");
    var mockSend = new Send("name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    var mockBody = gson.toJson(mockSend);
    event.setBody(mockBody);
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals(201, (int) result.getStatusCode());
    assert(content.contains(mockGeneratedId.toString()));
  }

  @Test
  public void postRequestFailure() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    when(sendProvider.addSend(any())).thenThrow(new SQLException());

    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("POST");
    var mockSend = new Send("name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    var mockBody = gson.toJson(mockSend);
    event.setBody(mockBody);
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);

    // assert
    assertEquals(500, (int) result.getStatusCode());
  }

  @Test
  public void putRequestSuccess() throws Exception {
    // mock
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockSend = new Send("name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    var mockId = UUID.randomUUID();
    var mockBody = gson.toJson(mockSend);

    // arrange
    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("PUT");
    event.setBody(mockBody);
    event.setPath("/send/" + mockId.toString());
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals(204, (int) result.getStatusCode());
    verify(sendProvider, times(1)).editSend(eq(mockId), any());
    assertNull(content);
  }

  @Test
  public void putRequestFailure() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    doThrow(new SQLException()).when(sendProvider).editSend(any(), any());

    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("PUT");
    event.setPath("/send/" + UUID.randomUUID());
    var mockSend = new Send("name", Style.SPORT, "grade", TickType.REDPOINT, "location");
    var mockBody = gson.toJson(mockSend);
    event.setBody(mockBody);
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);

    // assert
    assertEquals(500, (int) result.getStatusCode());
  }

  @Test
  public void deleteRequestSuccess() throws Exception {
    // mock
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    var mockId = UUID.randomUUID();

    // arrange
    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("DELETE");
    event.setPath("/send/" + mockId.toString());
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals(204, (int) result.getStatusCode());
    verify(sendProvider, times(1)).deleteSend(eq(mockId));
    assertNull(content);
  }

  @Test
  public void deleteRequestFailure() throws Exception {
    // arrange
    SendProvider sendProvider = mock(PostgresSendProvider.class);
    doThrow(new SQLException()).when(sendProvider).deleteSend(any());

    App app = new App(sendProvider);
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("DELETE");
    event.setPath("/send/" + UUID.randomUUID());
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);

    // assert
    assertEquals(500, (int) result.getStatusCode());
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
    assertEquals(400, (int) result.getStatusCode());
    assertNull(content);
  }
}
