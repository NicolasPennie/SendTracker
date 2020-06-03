package sendservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.Test;

public class AppTest {
  @Test
  public void successfulResponse() {
    // arrange
    App app = new App();
    var event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    var context = new TestContext();

    // act
    APIGatewayProxyResponseEvent result = app.handleRequest(event, context);
    String content = result.getBody();

    // assert
    assertEquals(result.getStatusCode().intValue(), 200);
    assertEquals(result.getHeaders().get("Content-Type"), "application/json");
    assertNotNull(content);
    assertTrue(content.contains("Welcome to SendTracker!"));
  }
}
