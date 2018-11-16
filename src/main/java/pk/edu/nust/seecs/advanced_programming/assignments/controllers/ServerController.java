package pk.edu.nust.seecs.advanced_programming.assignments.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pk.edu.nust.seecs.advanced_programming.assignments.helpers.ServerSocketHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    // FXML Attributes.
    @FXML public JFXButton startServerButton;
    @FXML public JFXButton stopServerButton;
    @FXML public JFXTextArea loggerTextArea;

    // Non FXML Attributes.
    private ServerSocketHandler socketHandler;

    // Methods.
    // Method to create and start server socket using socketCreator object.
    public void startServer() {
        startServerButton.setDisable(true);
        stopServerButton.setDisable(false);
        socketHandler = new ServerSocketHandler(8080);
    }

    // Method to stop the server.
    public void stopServer()  {
        startServerButton.setDisable(false);
        stopServerButton.setDisable(true);
        try {
            socketHandler.serverSocket.close();

            if (socketHandler.clientSocket != null) {
                socketHandler.clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to clear server logs.
    public void clearLogs() {
        loggerTextArea.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}