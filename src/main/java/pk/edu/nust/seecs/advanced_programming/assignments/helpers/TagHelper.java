package pk.edu.nust.seecs.advanced_programming.assignments.helpers;

public interface TagHelper {
    // Constants.

    // Methods signatures.
    // Method to setTag to a JSONObject.
    String setTag(String JSONObject, String tag);

    // Method to getTag from a JSONObject.
    String getTag(String JSONObject);

    // Method to get JSON object without tag.
    String getCleanObject(String JSONObject);
}