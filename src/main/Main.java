package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.dao.DBConnection;

import java.util.Objects;

/**
 * The main class which starts the JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(
                getClass().getResource("/main/view/LoginForm.fxml")
        ));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    /**
     * Sets the database connection, launches the application, then closes the database connection.
     * @param args arguments
     */
    public static void main(String[] args) {
        DBConnection.makeConnection("jdbc:mysql://localhost:3306/test_client_db", "jdbc", "password1");
        //To test for other locales (remove before submission)
//        Locale.setDefault(new Locale("fr", "FR"));
        launch(args);
//        Locale.setDefault(new Locale("en", "US"));
        DBConnection.closeConnection();
    }
}