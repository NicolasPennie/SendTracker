package sendservice.providers;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.google.gson.Gson;
import sendservice.models.Send;
import sendservice.models.SendDbCredentials;
import sendservice.models.Style;
import sendservice.models.TickType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PostgresSendProvider implements SendProvider {

    private final LambdaLogger logger;
    private final String connectionString;
    private final SendDbCredentials dbCredentials;
    private final Gson gson = new Gson();

    private interface ConnectionHandler {
        void handleConnection(Connection connection) throws SQLException;
    }

    public PostgresSendProvider(LambdaLogger logger) throws ClassNotFoundException {
        this.logger = logger;
        this.connectionString = getConnectionString();
        this.dbCredentials = getDbCredentials();
        Class.forName("org.postgresql.Driver");
    }

    /**
     * Queries all sends from the database.
     * @return List of all sends.
     * @throws SQLException
     */
    public List<Send> getAllSends() throws SQLException {
        var sql = "SELECT id, name, style, grade, tick_type, location FROM send";
        var results = new ArrayList<Send>();

        useDbConnection(connection -> {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var name = resultSet.getString("name");
                var style = Style.valueOf(resultSet.getString("style"));
                var grade = resultSet.getString("grade");
                var tickType = TickType.valueOf(resultSet.getString("tick_type"));
                var location = resultSet.getString("location");
                var send = new Send(id, name, style, grade, tickType, location);
                results.add(send);
            }
        });

        return results;
    }

    /**
     * Adds a send to the database.
     * @param send New send.
     * @return Generated ID.
     * @throws SQLException
     */
    public int addSend(Send send) throws SQLException {
        AtomicInteger primaryKey = new AtomicInteger(-1);
        var sql = "INSERT INTO send (name, style, grade, tick_type, location) " +
                "values (?, ?, ?, ?, ?) " +
                "RETURNING id";

        useDbConnection(connection -> {
            var statement = connection.prepareStatement(sql);
            statement.setString(1, send.getName());
            statement.setString(2, send.getStyle().toString());
            statement.setString(3, send.getGrade());
            statement.setString(4, send.getTickType().toString());
            statement.setString(5, send.getLocation());
            statement.execute();

            var resultSet = statement.getResultSet();
            resultSet.next();
            primaryKey.set(resultSet.getInt(1));
        });

        return primaryKey.get();
    }

    /**
     * Edits an existing send in the database.
     * @param id ID of the existing send.
     * @param send Send to update.
     * @throws Exception
     */
    public void editSend(int id, Send send) throws Exception {
        AtomicInteger updateCount = new AtomicInteger(0);
        var sql = "UPDATE send " +
                "SET (name, style, grade, tick_type, location) = (?, ?, ?, ?, ?) " +
                "WHERE id = ?";

        useDbConnection(connection -> {
            var statement = connection.prepareStatement(sql);
            statement.setString(1, send.getName());
            statement.setString(2, send.getStyle().toString());
            statement.setString(3, send.getGrade());
            statement.setString(4, send.getTickType().toString());
            statement.setString(5, send.getLocation());
            statement.setInt(6, id);
            statement.execute();
            updateCount.set(statement.getUpdateCount());
        });

        if (updateCount.get() == 0) {
            throw new Exception(String.format("No item with id %s exists.", id));
        }
    }

    public void deleteSend(int id) throws Exception {
        AtomicInteger updateCount = new AtomicInteger(0);
        var sql = "DELETE FROM send WHERE id = ?";

        useDbConnection(connection -> {
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
            updateCount.set(statement.getUpdateCount());
        });

        if (updateCount.get() == 0) {
            throw new Exception(String.format("No item with id %s exists.", id));
        }
    }

    private String getConnectionString() {
        String dbName = System.getenv("DB_NAME");
        String dbAddress = System.getenv("DB_ENDPOINT_ADDRESS");
        String dbPort = System.getenv("DB_ENDPOINT_PORT");
        return String.format("jdbc:postgresql://%s:%s/%s", dbAddress, dbPort, dbName);
    }

    private SendDbCredentials getDbCredentials() {
        String secretName = "send-db-prod";
        String region = "us-east-1";

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);

        try {
            var dbSecret = client.getSecretValue(getSecretValueRequest).getSecretString();
            return gson.fromJson(dbSecret, SendDbCredentials.class);
        } catch (Exception e) {
            logger.log("Error retrieving database credentials for SendDB: " + e.getMessage());
            throw e;
        }
    }


    private void useDbConnection(ConnectionHandler handler) throws SQLException {
        try (Connection connection = DriverManager.getConnection(connectionString,
                dbCredentials.getUsername(), dbCredentials.getPassword())) {
            handler.handleConnection(connection);
        }
    }
}
