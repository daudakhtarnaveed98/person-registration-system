// Package specification.
package pk.edu.nust.seecs.advanced_programming.assignments.models;

import javax.persistence.Entity;
import javax.persistence.Id;

// Person class definition.
@Entity
public class Person {
    // Attributes.
    private String firstName;
    private String lastName;
    @Id
    private String username;
    private String password;
    private String accessLevel;
    private String mobileNumber;

    // Constructors.
    // Default no-parameter constructor.
    public Person() {
        // Empty, no implementation.
    }

    // Constructor with six parameters.
    // Takes all (six) required arguments from user.
    // Creates a person object based on the arguments.
    public Person(String firstName, String lastName,
                  String username, String password,
                  String accessLevel, String mobileNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
        this.mobileNumber = mobileNumber;
    }

    // Methods.
    // Setters.
    // Setter for firstName.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Setter for lastName.
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Setter for username.
    public void setUsername(String username) {
        this.username = username;
    }

    // Setter for password.
    public void setPassword(String password) {
        this.password = password;
    }

    // Setter for accessLevel.
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    // Setter for mobileNumber.
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // Getters.
    // Getter for firstName.
    public String getFirstName() {
        return firstName;
    }

    // Getter for lastName.
    public String getLastName() {
        return lastName;
    }

    // Getter for username.
    public String getUsername() {
        return username;
    }

    // Getter for password.
    public String getPassword() {
        return password;
    }

    // Getter for accessLevel.
    public String getAccessLevel() {
        return accessLevel;
    }

    // Getter for mobileNumber.
    public String getMobileNumber() {
        return mobileNumber;
    }
}