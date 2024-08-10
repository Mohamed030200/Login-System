package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest {
    public static void main(String[] args) {
        // Test the database connection
        DBConnection dbConnection = DBConnection.getInstance(); // Obtain the singleton instance of DBConnection
        Connection connection = dbConnection.getConnection(); // Get the connection from the DBConnection instance

        if (connection != null) {
            // If the connection is successfully established
            System.out.println("Database connected successfully.");

            // Perform a database operation (e.g., executing a query)
            try (Statement statement = connection.createStatement()) {
                // Example query to select all records from the Accounts table
                String query = "SELECT * FROM Accounts";
                statement.execute(query); // Execute the query
                System.out.println("Database operation performed successfully.");
            } catch (SQLException e) {
                // Handle any SQL exceptions that occur during the database operation
                e.printStackTrace();
            }

            // Check if the connection is still open
            try {
                if (!connection.isClosed()) {
                    System.out.println("Database connection is still open.");
                }
            } catch (SQLException e) {
                // Handle any SQL exceptions that occur when checking the connection state
                e.printStackTrace();
            }

            // Optionally, close the connection
            // dbConnection.disconnect(); // Uncomment this line if you want to close the connection
        } else {
            // If the connection could not be established
            System.out.println("Failed to connect to the database.");
        }
    }
}
