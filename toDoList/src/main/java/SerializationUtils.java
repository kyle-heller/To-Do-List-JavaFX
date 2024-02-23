import java.io.*;

public class SerializationUtils {
    public static void serialize(TodoList todoList, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(todoList);
            System.out.println("Serialization completed. Object saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TodoList deserialize(String filename) {
        TodoList todoList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            todoList = (TodoList) ois.readObject();
            System.out.println("Deserialization completed. Object loaded from " + filename);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return todoList;
    }
}