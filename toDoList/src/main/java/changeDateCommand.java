import java.time.LocalDate;
import java.io.Serializable;

public class changeDateCommand implements Command, Serializable {
    private static final long serialVersionUID = 1L;
    private TodoList todoList;
    private Task task;
    private LocalDate oldDate;
    private LocalDate newDate;

    public changeDateCommand(TodoList todoList, Task task, LocalDate newDate) {
        this.todoList = todoList;
        this.task = task;
        this.oldDate = task.getDueDate();
        this.newDate = newDate;
    }

    @Override
    public void execute() {
        // Change the due date of the task
        task.setDueDate(newDate);
    }

    @Override
    public void undo() {
        // Revert the due date change to the previous date
        task.setDueDate(oldDate);
    }
}
