package Controller;

import java.sql.Connection;
import Model.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import DB.*;

public class Registration {
    // SQL query to insert a new username and hashed password into the Accounts table
    private static final String insertUsernamePasswordQ = "INSERT INTO Accounts (username,hashedPassword) Values (?,?)";
    private PreparedStatement insertUsernamePassword;
    private DBConnection dbconnection;

    // Constructor to initialize the DB connection and prepare the SQL statement
    public Registration() {
        dbconnection = DBConnection.getInstance();
        try {
            // Prepare the statement with the SQL query and enable returning generated keys
            insertUsernamePassword = dbconnection.getConnection().prepareStatement(insertUsernamePasswordQ,
                    Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }

    // Method to register a new user
    public void register(User user) throws SQLException {
        try {
            // Start a new database transaction
            dbconnection.startTransaction();

            // Set the username and hashed password parameters in the prepared statement
            insertUsernamePassword.setString(1, user.getUsername());
            insertUsernamePassword.setString(2, user.getHashedPassword());

            // Execute the insert statement to add the user to the database
            insertUsernamePassword.executeUpdate();

            // Commit the transaction to save the changes
            dbconnection.commitTransaction();
        } catch (Exception e) {
            // If an exception occurs, rollback the transaction to undo changes
            dbconnection.rollbackTransaction();
            // Throw a new SQLException with a custom message and the original exception
            throw new SQLException("Registering failed", e);
        }
    }
}
