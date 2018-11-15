package pk.edu.nust.seecs.advanced_programming.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pk.edu.nust.seecs.advanced_programming.assignments.helpers.ClientSocketHandler;

import java.net.URL;

public class Client extends Application {
    // Attributes.
    public static FXMLLoader loader;

    // Methods.
    // Start method.
    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader();
        URL location = getClass().getClassLoader().getResource("views/client.fxml");
        loader.setLocation(location);
        Parent root = loader.load();
        primaryStage.setTitle("TCP Client");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Stop method.
    @Override
    public void stop() {
        System.exit(0);
    }

    // Main method definition to start program execution.
    public static void main(String[] args) {
        launch(args);
    }
}