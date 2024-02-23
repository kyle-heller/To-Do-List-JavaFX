import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Task implements Serializable {
    // Static Fields
    static int taskCount;

    // Instance Fields
    private int taskID;
    private String taskName;
    private LocalDate dueDate;
    private transient BooleanProperty taskCompleted;
    private transient BooleanProperty importantHighlight;

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
        this.taskCompleted = new SimpleBooleanProperty(false);
        this.taskCompleted.addListener((obs, oldValue, newValue) -> {});
        this.taskCompleted.set(false);
        this.importantHighlight = new SimpleBooleanProperty(importantHighlight);
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

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getTaskID() {
        return this.taskID;
    }

    // Utility Methods
    @Override
    public String toString() {
        String completedText = isTaskCompleted() ? "Completed" : "Not Completed";
        String dueDateText = getDueDateString();
        return String.format("Task ID: %d, Name: %s, Status: %s, Due Date: %s", taskID, taskName, completedText, dueDateText);
    }

    public void setImportantHighlight(boolean b) {
        this.importantHighlight.set(b);
    }

    public void toggleImportantHighlight() {
        setImportantHighlight(!isImportantHighlight());
    }

    public boolean isImportantHighlight() {
        return this.importantHighlight.get();
    }

    // Custom serialization methods
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeBoolean(taskCompleted.get());
        out.writeBoolean(importantHighlight.get()); // Correctly writing the boolean value
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        taskCompleted = new SimpleBooleanProperty(in.readBoolean());

        // Re-initializing importantHighlight property correctly
        boolean importantHighlightValue = in.readBoolean();
        importantHighlight = new SimpleBooleanProperty(importantHighlightValue);
    }


    //To Organize

    public void changeName(String newName) {
        setTaskName(newName);
    }

    public void changeDate(LocalDate newDate) {
        this.dueDate = newDate;
    }
}
