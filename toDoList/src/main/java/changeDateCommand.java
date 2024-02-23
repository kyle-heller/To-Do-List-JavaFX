import java.time.LocalDate;
import java.io.Serializable;

/**
 * A command for changing the due date of a specified task within a todo list.
 * This command implements the Command pattern, allowing operations to be performed
 * and undone, promoting a more flexible and extensible system for managing tasks.
 */
public class changeDateCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L; // UID for serialization to ensure class compatibility.

    private TodoList todoList; // The todo list containing the task whose date is to be changed.
    private Task task; // The task whose due date is to be changed.
    private LocalDate oldDate; // The original due date of the task, for undoing the change.
    private LocalDate newDate; // The new due date to be set for the task.

    /**
     * Constructs a new command for changing the due date of a task.
     *
     * @param todoList The todo list containing the task.
     * @param task     The task whose due date is to be changed.
     * @param newDate  The new due date to set for the task.
     */
    public changeDateCommand(TodoList todoList, Task task, LocalDate newDate) {
        this.todoList = todoList;
        this.task = task;
        this.oldDate = task.getDueDate(); // Capture the old date for possible undo action.
        this.newDate = newDate;
    }

    /**
     * Executes the command by setting the new due date on the specified task.
     */
    @Override
    public void execute() {
        task.setDueDate(newDate);
    }

    /**
     * Undoes the command by reverting the task's due date to its original value.
     */
    @Override
    public void undo() {
        task.setDueDate(oldDate);
    }
}
