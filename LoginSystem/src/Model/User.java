package Model;

public class User {
    // Fields to store the username and the hashed password of the user
    private String username;
    private String hashedPassword;

    // Default constructor
    public User() {
    }

    // Constructor with parameters to initialize the username and hashedPassword fields
    public User(String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    // Overridden toString method to provide a string representation of the User object
    @Override
    public String toString() {
        return "User{" + "username='" + username + '\'' + ", hashedPassword='" + hashedPassword + '\'' + '}';
    }

    // Getter method for the username field
    public String getUsername() {
        return username;
    }

    // Setter method for the username field
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter method for the hashedPassword field
    public String getHashedPassword() {
        return hashedPassword;
    }

    // Setter method for the hashedPassword field
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
