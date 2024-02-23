import java.io.Serializable;

/**
 * A command object that changes the name of a task within a todo list.
 * This command supports undo functionality, allowing the task's name to be reverted to its original state.
 */
public class changeNameCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L; // UID for serialization to ensure class compatibility.

    private TodoList todoList; // The todo list containing the task.
    private Task task; // The task whose name is to be changed.
    private String oldName; // The original name of the task, for undoing the name change.
    private String newName; // The new name to be assigned to the task.

    /**
     * Constructs a new changeNameCommand for changing the name of a specified task.
     *
     * @param todoList The todo list containing the task whose name is to be changed.
     * @param task     The task whose name is to be changed.
     * @param newName  The new name to assign to the task.
     */
    public changeNameCommand(TodoList todoList, Task task, String newName) {
        this.todoList = todoList;
        this.task = task;
        this.oldName = task.getTaskName(); // Capture the old name for possible undo action.
        this.newName = newName;
    }

    /**
     * Executes the command by changing the task's name to the new name.
     */
    @Override
    public void execute() {
        task.changeName(newName);
    }

    /**
     * Undoes the command by reverting the task's name to its original name.
     */
    @Override
    public void undo() {
        task.changeName(oldName);
    }
}
