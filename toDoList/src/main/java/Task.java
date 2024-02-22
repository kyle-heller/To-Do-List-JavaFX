import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.util.Comparator;


public class Task {
    // Static Fields
    static int taskCount;

    // Instance Fields
    private int taskID;
    private String taskName;
    private boolean importantHighlight;
    private LocalDate dueDate;
    private BooleanProperty taskCompleted = new SimpleBooleanProperty(false);

    // Constructors
    public Task(String taskName) {
        this(taskName, null, false);
    }

    public Task(String taskName, LocalDate dueDate) {
        this(taskName, dueDate, false);
    }

    public Task(String taskName, LocalDate dueDate, boolean importantHighlight) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.importantHighlight = importantHighlight;
        this.taskID = assignTaskID();
    }

    // Static Methods
    private static int assignTaskID() {
        taskCount++;
        return taskCount;
    }

    // Property Accessors and Mutators
    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String newName) {
        this.taskName = newName;
    }

    public boolean isTaskCompleted() {
        return this.taskCompleted.get();
    }

    public void setTaskCompleted(boolean value) {
        this.taskCompleted.set(value);
    }

    public String getDueDateString() {
        return (dueDate != null) ? dueDate.toString() : "No due date";
    }

    public BooleanProperty taskCompletedProperty() {
        return this.taskCompleted;
    }

    // Utility Methods
    @Override
    public String toString() {
        String completedText = isTaskCompleted() ? "Completed" : "Not Completed";
        String dueDateText = getDueDateString();
        return String.format("Task ID: %d, Name: %s, Status: %s, Due Date: %s", taskID, taskName, completedText, dueDateText);
    }

    //To Organize

}
