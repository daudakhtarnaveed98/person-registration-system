package pk.edu.nust.seecs.advanced_programming.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    // Attributes.
    private static Stage primaryStage = new Stage();

    // Methods.
    // Start method.
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        URL location = getClass().getClassLoader().getResource("views/main.fxml");
        loader.setLocation(location);
        Parent root = loader.load();
        Main.primaryStage.setTitle("TCP Client");
        Main.primaryStage.setScene(new Scene(root, 707, 206));
        Main.primaryStage.setResizable(false);
        Main.primaryStage.show();
    }

    // Stop method.
    @Override
    public void stop() {
        System.exit(0);
    }

    // Getters.
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    // Main method definition to start program execution.
    public static void main(String[] args) {
        launch(args);
    }
}