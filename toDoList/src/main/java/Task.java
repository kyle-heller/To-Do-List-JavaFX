import java.time.LocalDate;
public class Task {
    static int taskCount;
    int taskID;
    String taskName;
    boolean completed, importantHighlight;
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
}
