import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Represents a list of tasks, providing functionality to add, remove, and complete tasks.
 * Also supports undoing the last command and persisting the list of tasks across sessions.
 */
public class TodoList implements Serializable {
    private static final long serialVersionUID = 1L; // Serialization version UID for compatibility.

    private ArrayList<Task> taskList; // List of tasks in the todo list.
    private Stack<Command> commandStack; // Stack of commands for undo functionality.

    /**
     * Constructs a new TodoList, initializing the list of tasks and loading any saved tasks.
     */
    public TodoList() {
        taskList = new ArrayList<>();
        commandStack = new Stack<>();
        loadTodoList(); // Attempt to load tasks from a serialized file.
    }

    /**
     * Executes a command related to task management and pushes it onto the undo stack.
     *
     * @param command The command to be executed.
     */
    public void performCommand(Command command) {
        command.execute();
        commandStack.push(command);
    }

    /**
     * Undoes the last action performed on the todo list, if possible.
     */
    public void undoLastAction() {
        if (!commandStack.isEmpty()) {
            Command lastCommand = commandStack.pop();
            lastCommand.undo();
        }
    }

    /**
     * Adds a new task to the todo list.
     *
     * @param newTask The task to be added.
     */
    public void addTask(Task newTask) {
        taskList.add(newTask);
    }

    /**
     * Removes a task from the todo list by task name.
     *
     * @param taskName The name of the task to be removed.
     */
    public void removeTask(String taskName) {
        taskList.removeIf(task -> task.getTaskName().equals(taskName));
    }

    /**
     * Marks a task as completed by task name.
     *
     * @param taskName The name of the task to be marked as completed.
     */
    public void completeTask(String taskName) {
        taskList.forEach(task -> {
            if (task.getTaskName().equals(taskName)) {
                task.setTaskCompleted(true);
            }
        });
    }

    /**
     * Retrieves the list of tasks.
     *
     * @return The list of tasks in the todo list.
     */
    public ArrayList<Task> getTasks() {
        return taskList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Task task : taskList) {
            result.append(task).append("\n");
        }
        return result.toString();
    }

    /**
     * Changes the name of a specific task.
     *
     * @param task The task whose name is to be changed.
     * @param newName The new name for the task.
     */
    public void changeName(Task task, String newName) {
        task.changeName(newName);
    }

    /**
     * Saves the current list of tasks to a file using serialization.
     */
    public void saveTodoList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("todo_list.ser"))) {
            oos.writeObject(taskList);
            System.out.println("Todo list saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the list of tasks from a file, if it exists, using deserialization.
     */
    private void loadTodoList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("todo_list.ser"))) {
            taskList = (ArrayList<Task>) ois.readObject();
            System.out.println("Todo list loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved todo list found. Starting with an empty list.");
        }
    }

    /**
     * The main method to start the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        TodoListGUI.main(args); // Delegate to the GUI class to start the application.
    }
}
