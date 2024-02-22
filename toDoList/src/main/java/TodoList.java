import java.util.ArrayList;

public class TodoList {
    private ArrayList<Task> taskList;

    public TodoList() {
        taskList = new ArrayList<>();
    }

    public void addTask(String taskName) {
        Task task = new Task(taskName);
        taskList.add(task);
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

    public static void main(String[] args) {
        TodoListGUI.main(null);
    }
}