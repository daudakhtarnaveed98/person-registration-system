package pk.edu.nust.seecs.advanced_programming.assignments.helpers;

import javafx.application.Platform;
import pk.edu.nust.seecs.advanced_programming.assignments.Client;
import pk.edu.nust.seecs.advanced_programming.assignments.controllers.ClientController;
import pk.edu.nust.seecs.advanced_programming.assignments.models.Person;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSocketHandler extends Thread {
    // Attributes.
    private DataInputStream clientInputStream;
    private DataOutputStream clientOutputStream;
    private String serverIpAddress;
    private int serverPortNumber;
    private ClientController clientController;
    public Socket socket;
    private PersonConverter personConverter;

    // Constructors.
    // Constructor with two parameters.
    public ClientSocketHandler(String serverIpAddress, int serverPortNumber) {
        this.serverIpAddress = serverIpAddress;
        this.serverPortNumber = serverPortNumber;
        clientController = Client.loader.getController();
        clientController.disconnect.setDisable(true);
        personConverter = new PersonConverter();
        clientController.connectionStatus = "connected.";
        start();
    }

    // Methods.
    // Method to send message to server.
    public void sendMessage(String message) {
        try {
            if (clientOutputStream != null)
                clientOutputStream.writeUTF(message);
        } catch (IOException e) {
            clientController.statusLabel.setText("Please connect first.");
            clientController.searchButton.setDisable(false);
        }
    }

    // Method to receive response.
    public void receive() {
        try {
            String response = clientInputStream.readUTF();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Overriding run method of Thread class.
    @Override
    public void run() {
        try {
            socket = new Socket(serverIpAddress, serverPortNumber);
            clientOutputStream = new DataOutputStream(socket.getOutputStream());
            clientInputStream = new DataInputStream(socket.getInputStream());

            Platform.runLater(() -> {
                clientController.statusLabel.setText("Connected to " + serverIpAddress + ":" + serverPortNumber);
                clientController.connect.setDisable(true);
                clientController.disconnect.setDisable(false);
            });

            while (true) {
                String responseFromServer = clientInputStream.readUTF();
                if (responseFromServer.equals("finished")) {
                    break;
                }

                if (responseFromServer.contains("found")) {
                    Person person = personConverter.JSONToPerson(personConverter.getCleanObject(responseFromServer));
                    Platform.runLater(() -> {
                        clientController.statusLabel.setText("Found User:" + person.getUsername());
                        clientController.updateButton.setDisable(false);
                        clientController.deleteButton.setDisable(false);
                        clientController.register.setDisable(true);
                        clientController.username.setDisable(true);
                        clientController.populate(person);
                    });
                }

                else if (responseFromServer.contains("none")) {
                    Platform.runLater(() -> clientController.statusLabel.setText("No record found for: " + clientController.searchField.getText()));
                }

                else if (responseFromServer.contains("successful")) {
                    Platform.runLater(() -> clientController.statusLabel.setText("User registered : " + clientController.username.getText()));
                }

                else if (responseFromServer.contains("updated")) {
                    Platform.runLater(() -> clientController.statusLabel.setText("User updated : " + clientController.username.getText()));
                }
                else if (responseFromServer.contains("duplicate")) {
                    Platform.runLater(() -> clientController.statusLabel.setText("User is duplicate : " + clientController.username.getText()));
                }
                else if (responseFromServer.contains("deleted")) {
                    Platform.runLater(() -> {
                        clientController.statusLabel.setText("User deleted : " + clientController.username.getText());
                        clientController.clearDetails();
                    });

                }
            }

        } catch (IOException e) {
            Platform.runLater(() -> {
                clientController.statusLabel.setText("Server not found.");
                clientController.disconnect.setDisable(true);
                clientController.connect.setDisable(false);
            });
        }
    }
}