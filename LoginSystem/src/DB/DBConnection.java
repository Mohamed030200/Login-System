package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    // Connection instance to manage database connection
    private Connection connection = null;
    
    // Singleton instance of DBConnection
    private static DBConnection dbConnection;

    // Database connection parameters
    private static final String driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String dbName = "Login_System";
    private static final String serverAddress = "DESKTOP-7T7UB95\\SQLEXPRESS"; // SQL Server instance
    private static final int serverPort = 1433; // Default SQL Server port
    private static final String userName = "sa"; // Database username
    private static final String password = "Password1!"; // Database password

    // Private constructor for singleton pattern, initializes the connection
    private DBConnection() {
        // Connection string to connect to the SQL Server database
        String connectionString = String.format(
            "jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s;encrypt=false", 
            serverAddress, serverPort, dbName, userName, password
        );
        try {
            // Load the JDBC driver
            Class.forName(driverClass);
            // Establish the connection
            connection = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load JDBC driver");
            e.printStackTrace(); // Handle or log the exception
        } catch (SQLException e) {
            // Handle connection failure with details
            System.err.println("Could not connect to database " + dbName + "@" + serverAddress + ":" + serverPort + " as user " + userName + " using password ******");
            System.out.println("Connection string was: " + connectionString.substring(0, connectionString.length() - password.length()) + "....");
            e.printStackTrace(); // Handle or log the exception
        }
    }

    // Public method to get the singleton instance of DBConnection
    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    // Start a new transaction by disabling auto-commit mode
    public void startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    // Commit the current transaction and re-enable auto-commit mode
    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    // Rollback the current transaction and re-enable auto-commit mode
    public void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    // Execute an insert statement and return the generated identity value
    public int executeInsertWithIdentity(PreparedStatement ps) throws SQLException {
        int res = -1;
        try {
            res = ps.executeUpdate(); // Execute the insert statement
            if (res > 0) {
                ResultSet rs = ps.getGeneratedKeys(); // Get the generated keys
                if (rs.next()) {
                    res = rs.getInt(1); // Retrieve the identity value
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception
        }
        return res;
    }

    // Execute an insert statement using a raw SQL string and return the generated identity value
    public int executeInsertWithIdentity(String sql) throws SQLException {
        System.out.println("DBConnection, Inserting: " + sql);
        int res = -1;
        try (Statement s = connection.createStatement()) {
            res = s.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS); // Execute the insert statement
            if (res > 0) {
                ResultSet rs = s.getGeneratedKeys(); // Get the generated keys
                if (rs.next()) {
                    res = rs.getInt(1); // Retrieve the identity value
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception
        }
        return res;
    }

    // Execute an update statement using a raw SQL string and return the affected row count
    public int executeUpdate(String sql) throws SQLException {
        System.out.println("DBConnection, Updating: " + sql);
        int res = -1;
        try (Statement s = connection.createStatement()) {
            res = s.executeUpdate(sql); // Execute the update statement
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception
        }
        return res;
    }

    // Getter method for the connection instance
    public Connection getConnection() {
        return connection;
    }

    // Close the database connection
    public void disconnect() {
        try {
            connection.close(); // Close the connection
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }
}
