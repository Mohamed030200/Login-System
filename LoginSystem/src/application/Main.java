package application;

import java.awt.event.ActionEvent;
import at.favre.lib.crypto.bcrypt.BCrypt;
import java.sql.SQLException;

import Model.*;
import Controller.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Main extends Application {
    // Create instances of Registration and Login classes to handle user data
    private Registration rg = new Registration();
    private Login lg = new Login();

    // Entry point of the application
    public static void main(String[] args) {
        launch(args);
    }

    // Default constructor
    public Main() {
    }

    // Start method is the main entry point for all JavaFX applications
    @Override
    public void start(Stage stage) throws Exception {

        // Load the image to be used as an icon and background
        Image image = new Image(getClass().getResourceAsStream("erlff48rp62d1.jpeg"));

        // Create an HBox layout to hold the left VBox and the login form
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setBackground(new Background(new BackgroundFill(Color.web("#D3D3D3"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Create the background image for the left VBox
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

        // Create the left VBox and set its background to the loaded image
        VBox leftBox = new VBox();
        leftBox.setBackground(new Background(backgroundImage));
        leftBox.setPrefWidth(300);

        // Create a GridPane for the login form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add a title text to the GridPane
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font(null, FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        // Add a label and text field for the username
        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        // Add a label and password field for the password
        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        // Create an HBox to hold the login and register buttons
        HBox hb = new HBox(5);
        Button btn = new Button("Login");
        Button btn1 = new Button("Register");

        // Set the action for the login button
        btn.setOnAction(event -> {
        	
        	 // Retrieve the entered username and password
            String username = userTextField.getText();
            String enteredPassword = pwBox.getText();

            // Check if username or password fields are empty
            if (username.isEmpty() || enteredPassword.isEmpty()) {
                System.out.println("Username or Password cannot be empty."); // Error message
                return; // Exit the event handler
            }

            // Retrieve the user based on the username
            User user = lg.findPassword(username);

            // If the user is null, it means the username doesn't exist
            if (user == null) {
                System.out.println("Username not found."); // Error message
                return; // Exit the event handler
            }

            // Retrieve the stored hashed password
            String storedpassWord = user.getHashedPassword();
            char[] enteredPasswordCharArray = enteredPassword.toCharArray();
            
            

            // Verify the entered password against the stored hashed password
            BCrypt.Result result = BCrypt.verifyer().verify(enteredPasswordCharArray, storedpassWord);

            if (result.verified) {
                System.out.println("Password matches!"); // Success message
            } else {
                System.out.println("Password does not match."); // Failure message
            }
        });

        // Set the action for the register button
        btn1.setOnAction(event -> {
            User user = new User();

            // Hash the entered password
            String hashedPW = pwBox.getText();
            String bcryptHashString = BCrypt.withDefaults().hashToString(12, hashedPW.toCharArray());

            // Set the hashed password and username for the user
            user.setHashedPassword(bcryptHashString);
            user.setUsername(userTextField.getText());
            try {
                // Register the user in the database
                rg.register(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Add the buttons to the HBox
        hb.getChildren().addAll(btn, btn1);

        // Add the HBox with buttons to the GridPane
        grid.add(hb, 1, 3);

        // Add the leftBox and GridPane to the main HBox
        hbox.getChildren().addAll(leftBox, grid);

        // Create a scene with the HBox and set it to the stage
        Scene scene = new Scene(hbox, 600, 400);
        stage.setTitle("Login Page");

        // Set the icon of the stage to the loaded image
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show(); // Display the stage
    }
}
