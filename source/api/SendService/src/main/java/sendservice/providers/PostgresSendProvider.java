package sendservice.providers;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.google.gson.Gson;
import sendservice.models.DbCredentials;
import sendservice.models.Send;
import sendservice.models.Style;
import sendservice.models.TickType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresSendProvider implements SendProvider {

    private final LambdaLogger logger;
    private final String connectionString;
    private final DbCredentials dbCredentials;
    private final Gson gson = new Gson();

    private interface DbResultHandler<T> {
        T handleResult(ResultSet resultset) throws SQLException;
    }

    public PostgresSendProvider(LambdaLogger logger) {
        this.logger = logger;
        this.connectionString = getConnectionString();
        this.dbCredentials = getDbCredentials();
    }

    public List<Send> getAllSends() throws SQLException {
        var query = "SELECT id, name, style, grade, tick_type, location FROM send";

        return executeDbQuery(query, resultSet -> {
            var id = resultSet.getString("id");
            var name = resultSet.getString("name");
            var style = Style.valueOf(resultSet.getString("style"));
            var grade = resultSet.getString("grade");
            var tickType = TickType.valueOf(resultSet.getString("tick_type"));
            var location = resultSet.getString("location");

            return new Send(id, name, style, grade, tickType, location);
        });
    }

    private String getConnectionString() {
        String dbName = System.getenv("DB_NAME");
        String dbAddress = System.getenv("DB_ENDPOINT_ADDRESS");
        String dbPort = System.getenv("DB_ENDPOINT_PORT");
        return String.format("jdbc:postgresql://%s:%s/%s", dbAddress, dbPort, dbName);
    }

    private DbCredentials getDbCredentials() {
        String secretName = "send-db-prod";
        String region = "us-east-1";

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);

        try {
            var dbSecret = client.getSecretValue(getSecretValueRequest).getSecretString();
            return gson.fromJson(dbSecret, DbCredentials.class);
        } catch (Exception e) {
            logger.log("Error retrieving database credentials for SendDB: " + e.getMessage());
            throw e;
        }
    }

    private <T> List<T> executeDbQuery(String query, DbResultHandler<T> handler) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error loading PostgreSQL driver");
        }

        var results = new ArrayList<T>();
        Connection connection = DriverManager.getConnection(connectionString, dbCredentials.getUsername(), dbCredentials.getPassword());
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            results.add(handler.handleResult(resultSet));
        }

        resultSet.close();
        statement.close();
        connection.close();

        return results;
    }
}
