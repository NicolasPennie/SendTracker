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

public class PostgresSendProvider implements SendProvider {

    private final LambdaLogger logger;
    private final String connectionString;
    private final SendDbCredentials dbCredentials;
    private final Gson gson = new Gson();

    private interface DbResultHandler<T> {
        T handleResult(ResultSet resultset) throws SQLException;
    }

    private interface DbStatementHandler {
        void handleStatement(PreparedStatement statement) throws SQLException;
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
        var query = "SELECT id, name, style, grade, tick_type, location FROM send";

        return executeDbQuery(query, resultSet -> {
            var id = resultSet.getInt("id");
            var name = resultSet.getString("name");
            var style = Style.valueOf(resultSet.getString("style"));
            var grade = resultSet.getString("grade");
            var tickType = TickType.valueOf(resultSet.getString("tick_type"));
            var location = resultSet.getString("location");

            return new Send(id, name, style, grade, tickType, location);
        });
    }

    /**
     * Adds a send to the database.
     * @param send New send.
     * @return Generated ID.
     * @throws SQLException
     */
    public int addSend(Send send) throws SQLException {
        var sql = "INSERT INTO send (name, style, grade, tick_type, location) " +
                "values (?, ?, ?, ?, ?) " +
                "RETURNING id";

        var results = executeDbUpdate(sql, statement -> {
            statement.setString(1, send.getName());
            statement.setString(2, send.getStyle().toString());
            statement.setString(3, send.getGrade());
            statement.setString(4, send.getTickType().toString());
            statement.setString(5, send.getLocation());
        }, resultSet -> resultSet.getInt(1));

        return results.get(0);
    }

    /**
     * Edits an existing send in the database.
     * @param send Send to update. The ID field must be populated.
     * @throws SQLException
     */
    public void editSend(Send send) throws SQLException {
        var sql = "UPDATE send " +
                "SET (name, style, grade, tick_type, location) = (?, ?, ?, ?, ?)" +
                "WHERE id = ?";

        executeDbUpdate(sql, statement -> {
            statement.setString(1, send.getName());
            statement.setString(2, send.getStyle().toString());
            statement.setString(3, send.getGrade());
            statement.setString(4, send.getTickType().toString());
            statement.setString(5, send.getLocation());
            statement.setInt(6, send.getId());
        });
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

    /**
     * Executes a query against the database.
     * @param query SQL query.
     * @param handler Function that processes the query results.
     * @param <T> Model that the query results will be bound against.
     * @return Query results as a list of T.
     * @throws SQLException
     */
    private <T> List<T> executeDbQuery(String query, DbResultHandler<T> handler) throws SQLException {
        var results = new ArrayList<T>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(connectionString,
                    dbCredentials.getUsername(), dbCredentials.getPassword());
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                results.add(handler.handleResult(resultSet));
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return results;
    }

    /**
     * Executes a SQL statement to update the database.
     * @param sql Prepared SQL statement.
     * @param statementHandler Handles the prepared SQL statement. Used to assign values to statement parameters.
     * @throws SQLException
     */
    private void executeDbUpdate(String sql, DbStatementHandler statementHandler) throws SQLException {
        executeDbUpdate(sql, statementHandler, null);
    }

    /**
     * Executes a SQL statement to update the database.
     * @param sql Prepared SQL statement.
     * @param statementHandler Handles the prepared SQL statement. Used to assign values to statement parameters.
     * @param resultHandler Handles the resultSet that contains the statement results.
     * @return Update results as a list of T.
     * @throws SQLException
     */
    private <T> List<T> executeDbUpdate(String sql, DbStatementHandler statementHandler, DbResultHandler<T> resultHandler)
            throws SQLException {
        var results = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(connectionString,
                    dbCredentials.getUsername(), dbCredentials.getPassword());
            statement = connection.prepareStatement(sql);
            statementHandler.handleStatement(statement);
            statement.execute();

            if (resultHandler != null) {
                resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    results.add(resultHandler.handleResult(resultSet));
                }
            }
        }
        finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return results;
    }
}
