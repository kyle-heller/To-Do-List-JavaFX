import java.io.*;

/**
 * Utility class for serializing and deserializing TodoList objects.
 * Provides static methods to save a TodoList to a file and to load a TodoList from a file.
 */
public class SerializationUtils {

    /**
     * Serializes a TodoList object to a specified file.
     *
     * @param todoList The TodoList object to be serialized.
     * @param filename The name of the file where the TodoList object will be saved.
     */
    public static void serialize(TodoList todoList, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(todoList);
            System.out.println("Serialization completed. Object saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error during serialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Deserializes a TodoList object from a specified file.
     *
     * @param filename The name of the file from which the TodoList object will be loaded.
     * @return The deserialized TodoList object, or null if the deserialization fails.
     */
    public static TodoList deserialize(String filename) {
        TodoList todoList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            todoList = (TodoList) ois.readObject();
            System.out.println("Deserialization completed. Object loaded from " + filename);
        } catch (IOException e) {
            System.err.println("Error during deserialization: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found during deserialization: " + e.getMessage());
            e.printStackTrace();
        }
        return todoList;
    }
}
