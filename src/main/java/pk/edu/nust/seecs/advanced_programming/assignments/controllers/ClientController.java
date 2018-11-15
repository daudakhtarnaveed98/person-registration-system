package pk.edu.nust.seecs.advanced_programming.assignments.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pk.edu.nust.seecs.advanced_programming.assignments.helpers.ClientSocketHandler;
import pk.edu.nust.seecs.advanced_programming.assignments.helpers.PersonConverter;
import pk.edu.nust.seecs.advanced_programming.assignments.models.Person;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ClientController implements Initializable {
    // FXML Attributes.
    @FXML public Label statusLabel;
    @FXML public JFXTextField firstName;
    @FXML public JFXTextField lastName;
    @FXML public JFXTextField username;
    @FXML public JFXTextField accessLevel;
    @FXML public JFXTextField mobileNumber;
    @FXML public JFXPasswordField password;
    @FXML public JFXButton register;
    @FXML public JFXButton connect;
    @FXML public JFXButton disconnect;
    @FXML public JFXTextField searchField;
    @FXML public JFXButton deleteButton;
    @FXML public JFXButton updateButton;
    @FXML public JFXButton searchButton;

    // Non FXML Attributes.
    private ClientSocketHandler clientSocketHandler;
    private PersonConverter personConverter;
    public String connectionStatus = null;

    // Constructors.
    // No custom constructors.

    // Methods.
    // Method to check if all the fields of the form are filled.
    private boolean isFormFilled() {
        return !firstName.getText().isEmpty() && !lastName.getText().isEmpty() && !username.getText().isEmpty()
                && !password.getText().isEmpty() && !accessLevel.getText().isEmpty() && !mobileNumber.getText().isEmpty();
    }

    // Method to check if mobile number is valid.
    private boolean isMobileNumberValid() {
        return this.mobileNumber.getText().matches("[0][3][0-9]{2}-[0-9]{7}");
    }

    // Method to handle user registration.
    public void registerUser() {
        // Create person object based on values.
        Person person = new Person(firstName.getText().toLowerCase(), lastName.getText().toLowerCase(),
                username.getText().toLowerCase(), password.getText().toLowerCase(), accessLevel.getText().toLowerCase(),
                mobileNumber.getText().toLowerCase());

        // Convert person to JSON and set post tag.
        String taggedPersonJSON = personConverter.setTag(personConverter.personToJSON(person), "post");

        // Sending message to server.
        clientSocketHandler.sendMessage(taggedPersonJSON);
        }

    // Method to handle user search.
    public void getUser() {
        // Getting username from textfield.
        String userName = searchField.getText().toLowerCase();



         if (userName.equals("")) {
             statusLabel.setText("Please enter username");
         } else {

             // Setting get tag.
             userName = personConverter.setTag(userName, "get");

             // Sending to server.
             if (clientSocketHandler != null)
                 clientSocketHandler.sendMessage(userName);
             else statusLabel.setText("Please connect to server.");
         }
    }

    // Method to update user
    public void updateUser() {
        // Create person object based on values.
        Person person = new Person(firstName.getText().toLowerCase(), lastName.getText().toLowerCase(),
                username.getText().toLowerCase(), password.getText().toLowerCase(), accessLevel.getText().toLowerCase(),
                mobileNumber.getText().toLowerCase());

        // Convert person to JSON and set post tag.
        String taggedPersonJSON = personConverter.setTag(personConverter.personToJSON(person), "put");

        // Sending message to server.
        clientSocketHandler.sendMessage(taggedPersonJSON);
    }

    // Method to enable the register button.
    public void toggleRegisterButton() {
        if (isFormFilled() && isMobileNumberValid()) {
                register.setDisable(false);
                register.setText("Register");
        }
        if (!username.isDisabled() && isMobileNumberValid() && isFormFilled()) {
            register.setText("Register");
        }
        else {
            register.setDisable(true);
            register.setText("Please Fill Complete Form");
        }
    }

    // Method to connect with server.
    public void connect() {
        connect.setDisable(true);
        disconnect.setDisable(false);
        clientSocketHandler = new ClientSocketHandler("127.0.0.1", 8080);
        Platform.runLater(()-> {
            if (isFormFilled() && isMobileNumberValid() && clientSocketHandler.socket != null) {
                register.setDisable(false);
                register.setText("Register");
            }
        });

    }

    // Method to disconnect from server.
    public void disconnect()  {
        disconnect.setDisable(true);
        connect.setDisable(false);
        try {
            if (clientSocketHandler.socket != null)
                clientSocketHandler.socket.close();
            clearDetails();
            statusLabel.setText("Connection terminated.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to populate fields with person.
    public void populate(Person person) {
        username.setText(person.getUsername());
        firstName.setText(person.getFirstName());
        password.setText(person.getPassword());
        lastName.setText(person.getLastName());
        accessLevel.setText(person.getAccessLevel());
        mobileNumber.setText(person.getMobileNumber());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personConverter = new PersonConverter();
        disconnect.setDisable(true);
        statusLabel.setText("Please connect to server.");
    }

    // Method to clear user details from text fields.
    public void clearDetails() {
        username.clear();
        password.clear();
        firstName.clear();
        lastName.clear();
        mobileNumber.clear();
        accessLevel.clear();
        register.setDisable(true);
        register.setText("Please Fill Complete Form");
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        username.setDisable(false);
    }

    // Method to delete user.
    public void deleteUser() {
        String userName = username.getText();
        userName = personConverter.setTag(userName, "delete");
        clientSocketHandler.sendMessage(userName);
    }
}