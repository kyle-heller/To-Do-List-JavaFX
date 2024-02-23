import java.io.Serializable;
public class changeNameCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L;
    private TodoList todoList;
    private Task task;
    private String oldName;
    private String newName;

    public changeNameCommand(TodoList todoList, Task task, String newName) {
        this.todoList = todoList;
        this.task = task;
        this.oldName = task.getTaskName();
        this.newName = newName;
    }

    @Override
    public void execute() {
        // Change the name of the task
        task.changeName(newName);    }

    @Override
    public void undo() {
        // Revert the name change to the previous name
        task.changeName(oldName);
    }
}
