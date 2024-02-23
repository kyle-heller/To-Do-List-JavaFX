import java.io.Serializable;

/**
 * Implements a command to remove a task from a todo list. This command is part of the Command pattern,
 * allowing for operations to be encapsulated as objects. This enables actions like undo functionality.
 */
public class removeTaskCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L; // Ensures compatibility during the serialization process.

    private TodoList todoList; // The todo list from which the task will be removed.
    private Task task; // The task to be removed.
    private int taskIndex; // The original index of the task within the list, for restoration purposes on undo.

    /**
     * Constructs a removeTaskCommand for a specific task within a given todo list.
     *
     * @param todoList The todo list containing the task to be removed.
     * @param task The task to be removed from the todo list.
     */
    public removeTaskCommand(TodoList todoList, Task task) {
        this.todoList = todoList;
        this.task = task;
        // Save the current index of the task to allow for precise restoration if the action is undone.
        this.taskIndex = todoList.getTasks().indexOf(task);
    }

    /**
     * Executes the task removal operation by removing the task from the todo list.
     */
    @Override
    public void execute() {
        todoList.getTasks().remove(task);
    }

    /**
     * Undoes the task removal operation by restoring the task to its original position in the todo list.
     * If the original position is no longer valid (e.g., the list has changed size), the task is added to the end.
     */
    @Override
    public void undo() {
        if (taskIndex >= 0 && taskIndex <= todoList.getTasks().size()) {
            todoList.getTasks().add(taskIndex, task);
        } else {
            // If the original position is no longer valid, add the task to the end of the list.
            todoList.getTasks().add(task);
        }
    }
}
