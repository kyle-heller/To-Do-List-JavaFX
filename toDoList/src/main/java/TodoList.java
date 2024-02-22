import java.util.ArrayList;

public class TodoList {
    private ArrayList<Task> taskList;

    // Constructor
    public TodoList() {
        taskList = new ArrayList<>();
    }

    // Action Methods
    public void addTask(Task newTask) {
        taskList.add(newTask);
    }

    public void removeTask(String taskName) {
        taskList.removeIf(task -> task.getTaskName().equals(taskName));
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

    // Main
    public static void main(String[] args) {
        TodoListGUI.main(null);
    }
}
