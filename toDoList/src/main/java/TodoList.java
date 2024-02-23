import java.util.ArrayList;
import java.util.Stack;
import java.io.Serializable;
import java.io.*;

public class TodoList implements Serializable {
    private ArrayList<Task> taskList;

    // Constructor
    public TodoList() {
        taskList = new ArrayList<>();
        loadTodoList();
    }

    // Action Methods
    private Stack<Command> commandStack = new Stack<>();


    public void performCommand(Command command) {
        command.execute();
        commandStack.push(command);
    }

    // Method to undo the last command
    public void undoLastAction() {
        if (!commandStack.isEmpty()) {
            Command lastCommand = commandStack.pop();
            lastCommand.undo();
        }
    }



    public void addTask(Task newTask) {
        taskList.add(newTask);
    }

    public void removeTask(String taskName) {
        for (Task task : taskList) {
            if (task.getTaskName().equals(taskName)) {
                Command removeCommand = new removeTaskCommand(this, task);
                removeCommand.execute();
                commandStack.push(removeCommand);
                break;
            }
        }
    }


    public void completeTask(String taskName) {
        taskList.forEach(task -> {
            if (task.getTaskName().equals(taskName)) {
                task.setTaskCompleted(true);
            }
        });
    }

    // Getter Methods
    public ArrayList<Task> getTasks() {
        return taskList;
    }

    // Utility Methods
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Task task : taskList) {
            result.append(task).append("\n");
        }
        return result.toString();
    }

    public void changeName(Task task, String newName) {
        for (Task t : taskList) {
            if (t.getTaskID() == task.getTaskID()) {
                t.changeName(newName);
                break;
            }
        }
    }

    public void saveTodoList() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("todo_list.ser"))) {
            oos.writeObject(taskList);
            System.out.println("Todo list saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTodoList() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("todo_list.ser"))) {
            taskList = (ArrayList<Task>) ois.readObject();
            System.out.println("Todo list loaded.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No saved todo list found. Starting with an empty list.");
        }
    }

    // Main
    public static void main(String[] args) {
        TodoListGUI.main(args);
    }
}
