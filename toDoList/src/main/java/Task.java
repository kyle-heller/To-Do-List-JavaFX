import java.time.LocalDate;
public class Task {
    static int taskCount;
    int taskID;
    String taskName;
    boolean taskCompleted, importantHighlight;
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
        this.taskCompleted = false;
        taskID = assignTaskID();
    }

    private static int assignTaskID() {
        taskCount++;
        return taskCount;
    }

    public String getTaskName() { return this.taskName; }

    public void setTaskName(String newName) { this.taskName = newName; }
    public void setTaskCompleted() { this.setTaskCompleted(true); }
    public void setTaskCompleted(boolean value) { this.taskCompleted = value; }



    public String toString()
    {
        return taskID + " " + taskName + " " + taskCompleted;
    }
}
