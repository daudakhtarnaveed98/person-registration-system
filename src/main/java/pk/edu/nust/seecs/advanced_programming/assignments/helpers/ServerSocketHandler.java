package pk.edu.nust.seecs.advanced_programming.assignments.helpers;

import javafx.application.Platform;
import pk.edu.nust.seecs.advanced_programming.assignments.Server;
import pk.edu.nust.seecs.advanced_programming.assignments.controllers.ServerController;
import pk.edu.nust.seecs.advanced_programming.assignments.models.Person;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketHandler extends Thread {
    // Attributes.
    public ServerSocket serverSocket;
    public Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private int port;
    private ServerController serverController;
    private PersonConverter personConverter;
    private DBHandler dbHandler;

    // Constructors.
    // Constructor with one parameter, port.
    public ServerSocketHandler(int port) {
        this.port = port;
        serverController = Server.loader.getController();
        personConverter = new PersonConverter();
        dbHandler = new DBHandler();
        start();
    }

    // Methods.
    // Overriding run method of thread class.
    @Override
    public void run() {
        // Creating server socket.
        try {
            serverSocket = new ServerSocket(port);
            Platform.runLater(() -> serverController.loggerTextArea.appendText("Server started @ "+serverSocket.getInetAddress()+":"+serverSocket.getLocalPort()+"\n"));
        } catch (IOException e) {
            Platform.runLater(() -> serverController.loggerTextArea.appendText("Cannot start server.\n"));
            serverController.startServerButton.setDisable(false);
        }

        // Accepting connection from client.
        try {
            Platform.runLater(() -> serverController.loggerTextArea.appendText("Waiting for client.\n"));
            clientSocket = serverSocket.accept();
            Platform.runLater(() -> serverController.loggerTextArea.appendText("Client accepted : " + clientSocket.getInetAddress() + ":" + clientSocket.getLocalPort() + "\n"));
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                String receivedMessage = inputStream.readUTF();
                if (receivedMessage.equals("complete.")) {
                    break;
                }

                Platform.runLater(() -> {
                    serverController.loggerTextArea.appendText("Client sent: " + receivedMessage + "\n");
                    String tag = personConverter.getTag(receivedMessage);

                    switch (tag) {
                        case "post": {
                            Person person = personConverter.JSONToPerson(personConverter.getCleanObject(receivedMessage));
                            if (dbHandler.get(person.getUsername()) == null) {
                                dbHandler.register(person);
                                try {
                                    outputStream.writeUTF("successful");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    outputStream.writeUTF("duplicate");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        }
                        case "get": {
                            Person person = dbHandler.get(personConverter.removeTag(receivedMessage));
                            if (person != null) {
                                String personJSON = personConverter.personToJSON(person);
                                String taggedPersonJSON = personConverter.setTag(personJSON, "found");

                                try {
                                    outputStream.writeUTF(taggedPersonJSON);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    outputStream.writeUTF("none");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        }
                        case "put": {
                            Person person = personConverter.JSONToPerson(personConverter.getCleanObject(receivedMessage));
                            dbHandler.update(person);
                            try {
                                outputStream.writeUTF("updated");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        case "delete":
                            dbHandler.delete(personConverter.removeTag(receivedMessage));
                            try {
                                outputStream.writeUTF("deleted");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }

                });
            }

        } catch (IOException e) {
            // Closing inputStream.
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            // Closing outputStream.
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            // Closing clientSocket.
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            // Closing serverSocket.
            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            // Appending message to loggerTextArea.
            Platform.runLater(() -> {
                serverController.loggerTextArea.appendText("Client disconnected.\n");
                serverController.loggerTextArea.appendText("Server stopped successfully.\n");
                serverController.startServerButton.setDisable(false);
                serverController.stopServerButton.setDisable(true);
            });
        }
    }
}