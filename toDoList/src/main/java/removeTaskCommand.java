import java.io.Serializable;
public class removeTaskCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L;
    private TodoList todoList;
    private Task task;
    private int taskIndex; // The original index where the task was, in case we need to restore it

    public removeTaskCommand(TodoList todoList, Task task) {
        this.todoList = todoList;
        this.task = task;
        this.taskIndex = todoList.getTasks().indexOf(task); // Save the current index of the task
    }

    @Override
    public void execute() {
        // Remove the task from the todoList
        todoList.getTasks().remove(task);
    }

    @Override
    public void undo() {
        // Add the task back at the original position
        if (taskIndex >= 0 && taskIndex <= todoList.getTasks().size()) {
            todoList.getTasks().add(taskIndex, task);
        } else {
            // If the original position is no longer valid, just add it to the end
            todoList.getTasks().add(task);
        }
    }
}