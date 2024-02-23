import java.io.Serializable;

/**
 * Implements a command to add a task to a todo list.
 * This command supports undo functionality, allowing for the removal of the recently added task.
 */
public class addTaskCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L; // Used for object serialization.

    private TodoList todoList; // The todo list to which the task will be added.
    private Task task; // The task to be added to the todo list.

    /**
     * Constructs an addTaskCommand with a specific TodoList and Task.
     *
     * @param todoList The TodoList to which the task will be added.
     * @param task The Task to be added to the TodoList.
     */
    public addTaskCommand(TodoList todoList, Task task) {
        this.todoList = todoList;
        this.task = task;
    }

    /**
     * Executes the command by adding the task to the todo list.
     */
    @Override
    public void execute() {
        todoList.addTask(task);
    }

    /**
     * Undoes the command by removing the task from the todo list.
     * This method relies on the task's name for removal, assuming task names are unique within the list.
     */
    @Override
    public void undo() {
        todoList.removeTask(task.getTaskName());
    }
}
