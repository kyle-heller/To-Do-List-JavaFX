import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Task {
    static int taskCount;
    int taskID;
    String taskName;
    boolean importantHighlight;

    private BooleanProperty taskCompleted = new SimpleBooleanProperty(false);

    public BooleanProperty taskCompletedProperty() {
        return taskCompleted;
    }

    public boolean isTaskCompleted() {
        return taskCompleted.get();
    }

    public void setTaskCompleted(boolean value) {
        this.taskCompleted.set(value);
    }

    LocalDate dueDate;
//    example: LocalDate dueDate = LocalDate.of(2024, 1, 31); // YYYY-MM-DD

    public Task(String taskName) {
        this(taskName, null);
    }
    public Task(String taskName, LocalDate dueDate) {
        this(taskName, dueDate, false);
    }
    public Task(String taskName, LocalDate dueDate, boolean importantHighlight) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.importantHighlight = importantHighlight;
        taskID = assignTaskID();
    }

    private static int assignTaskID() {
        taskCount++;
        return taskCount;
    }

    public String getTaskName() { return this.taskName; }



    public void setTaskName(String newName) { this.taskName = newName; }


    public String toString()
    {
        return taskID + " " + taskName + " " + taskCompleted;
    }
}
