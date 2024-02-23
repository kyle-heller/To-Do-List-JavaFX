import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a task with a name, due date, completion status, and an importance highlight flag.
 * It includes functionality for serialization to persist task state across application sessions.
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    // Static field to track the total number of tasks created
    static int taskCount;

    // Unique ID for the task, derived from the static taskCount
    private int taskID;
    // Descriptive name of the task
    private String taskName;
    // Optional due date for the task; can be null if not set
    private LocalDate dueDate;
    // Indicates whether the task has been completed; marked transient for custom serialization
    private transient BooleanProperty taskCompleted;
    // Flag to highlight the task as important; marked transient for custom serialization
    private transient BooleanProperty importantHighlight;

    /**
     * Constructs a new Task with a specified name. By default, the task is not highlighted and has no due date.
     *
     * @param taskName The name of the task.
     */
    public Task(String taskName) {
        this(taskName, null, false);
    }

    /**
     * Constructs a new Task with a specified name and due date. By default, the task is not highlighted.
     *
     * @param taskName The name of the task.
     * @param dueDate  The due date of the task.
     */
    public Task(String taskName, LocalDate dueDate) {
        this(taskName, dueDate, false);
    }

    /**
     * Constructs a new Task with a specified name, due date, and highlight status.
     *
     * @param taskName          The name of the task.
     * @param dueDate           The due date of the task. Can be {@code null} if the task has no due date.
     * @param importantHighlight A boolean flag indicating whether the task is highlighted as important.
     */
    public Task(String taskName, LocalDate dueDate, boolean importantHighlight) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.taskCompleted = new SimpleBooleanProperty(false);
        this.importantHighlight = new SimpleBooleanProperty(importantHighlight);
        this.taskID = assignTaskID();
    }

    /**
     * Generates and assigns a unique ID to the task based on a static task count.
     * This method increments the taskCount each time a new task is created.
     *
     * @return The unique task ID assigned to the task.
     */
    private static int assignTaskID() {
        taskCount++;
        return taskCount;
    }

    // Accessors and mutators for task properties

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

    public boolean isImportantHighlight() {
        return this.importantHighlight.get();
    }

    public void setImportantHighlight(boolean value) {
        this.importantHighlight.set(value);
    }

    /**
     * Provides a string representation of the task, including its ID, name, completion status, and due date.
     * This method is overridden to customize the string representation of a task object.
     *
     * @return A string detailing the task's properties.
     */
    @Override
    public String toString() {
        String completedText = isTaskCompleted() ? "Completed" : "Not Completed";
        return String.format("Task ID: %d, Name: %s, Status: %s, Due Date: %s",
                taskID, taskName, completedText, getDueDateString());
    }

    public String getDueDateString() {
        return (dueDate != null) ? dueDate.toString() : "No due date";
    }

    public void toggleImportantHighlight() {
        setImportantHighlight(!isImportantHighlight());
    }

    // Additional utility methods for task manipulation

    public void changeName(String newName) {
        setTaskName(newName);
    }

    public void changeDate(LocalDate newDate) {
        setDueDate(newDate);
    }

    // Custom serialization methods to handle the transient properties

    /**
     * Custom serialization method to manually serialize the transient fields along with the default fields.
     *
     * @param out The ObjectOutputStream to write the object to.
     * @throws IOException If an I/O error occurs during serialization.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeBoolean(taskCompleted.get());
        out.writeBoolean(importantHighlight.get());
    }

    /**
     * Custom deserialization method to manually deserialize the transient fields along with the default fields.
     *
     * @param in The ObjectInputStream to read the object from.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        taskCompleted = new SimpleBooleanProperty(in.readBoolean());
        importantHighlight = new SimpleBooleanProperty(in.readBoolean());
    }
}
