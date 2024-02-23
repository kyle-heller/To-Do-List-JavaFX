import java.io.Serializable;
public class addTaskCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L;
    private TodoList todoList;
    private Task task;

    public addTaskCommand(TodoList todoList, Task task) {
        this.todoList = todoList;
        this.task = task;
    }

    @Override
    public void execute() {
        todoList.addTask(task);
    }

    @Override
    public void undo() {
        todoList.removeTask(task.getTaskName());
    }
}

