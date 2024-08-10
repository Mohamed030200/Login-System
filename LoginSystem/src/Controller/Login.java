package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DB.DBConnection;
import Model.User;

public class Login {
    // SQL query to find the username and hashed password in the Accounts table
    private static final String findPasswordQ = "SELECT username, hashedPassword FROM Accounts WHERE username = ?";
    private PreparedStatement findPassword;

    // Constructor to prepare the SQL statement
    public Login() {
        try {
            // Prepare the statement using the connection from the DBConnection class
            findPassword = DBConnection.getInstance().getConnection().prepareStatement(findPasswordQ);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }

    // Method to find a user based on their username
    public User findPassword(String username) {
        User user = null;
        try {
            // Set the username parameter in the prepared statement
            findPassword.setString(1, username);
            // Execute the query and get the result set
            try (ResultSet rs = findPassword.executeQuery()) {
                if (rs.next()) {
                    // If a record is found, build a User object from the result set
                    user = buildObject(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
        // Return the user object (null if not found)
        return user;
    }

    // Helper method to build a User object from the result set
    private User buildObject(ResultSet rs) throws SQLException {
        return new User(
            rs.getString("username"),       // Retrieve and set the username
            rs.getString("hashedPassword")  // Retrieve and set the hashed password
        );
    }
}
