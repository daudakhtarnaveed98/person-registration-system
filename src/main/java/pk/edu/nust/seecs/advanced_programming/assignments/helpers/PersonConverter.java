// Package specification.
package pk.edu.nust.seecs.advanced_programming.assignments.helpers;

// Importing libraries.
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pk.edu.nust.seecs.advanced_programming.assignments.models.Person;
import java.io.IOException;

// PersonConverter class definition.
public class PersonConverter implements TagHelper {
    // Attributes.
    private ObjectMapper mapper;

    // Constructors.
    // Default no-parameter constructor.
    public PersonConverter() {
        this.mapper = new ObjectMapper();
    }

    // Methods.
    // Conversion methods.
    // Method to convert Person object to JSON.
    public String personToJSON(Person person) {
        try {
            return  mapper.writeValueAsString(person);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to convert JSON to Person object.
    Person JSONToPerson(String JSONString) {
        try {
            return mapper.readValue(JSONString, Person.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Tagging methods (from TagHelper interface).
    // Method to setTag to a JSONObject.
    public String setTag(String JSONObject, String tag) {
        return tag + ":" + JSONObject;
    }

    // Method to getTag from a JSONObject.
    public String getTag(String JSONObject) {
        return JSONObject.substring(0, JSONObject.indexOf(":"));
    }

    // Method to get JSON object without tag.
    public String getCleanObject(String JSONObject) {
        return JSONObject.substring(JSONObject.indexOf("{"));
    }

    // Method to remove tag from a String.
    public String removeTag(String taggedString) {
        return taggedString.substring(taggedString.indexOf(":") + 1);
    }
}