import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

public class TodoListTest {

    private TodoList todoList;

    @BeforeEach
    public void setUp() {
        todoList = new TodoList();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Learn Java", LocalDate.now());
        todoList.addTask(task);
        Assertions.assertTrue(todoList.getTasks().contains(task), "The task should be added successfully");
    }

    @Test
    public void testRemoveTask() {
        Task task = new Task("Complete Homework", LocalDate.now());
        todoList.addTask(task);
        todoList.removeTask(task.getTaskName());
        Assertions.assertFalse(todoList.getTasks().contains(task), "The task should be removed successfully");
    }

    @Test
    public void testUndoAddTask() {
        Task task = new Task("Go for a run", LocalDate.now());
        Command addTask = new addTaskCommand(todoList, task);
        todoList.performCommand(addTask);
        todoList.undoLastAction();
        Assertions.assertFalse(todoList.getTasks().contains(task), "The add task action should be undone successfully");
    }

    @Test
    public void testUndoRemoveTask() {
        Task task = new Task("Read a book", LocalDate.now());
        todoList.addTask(task);
        Command removeTask = new removeTaskCommand(todoList, task);
        todoList.performCommand(removeTask);
        todoList.undoLastAction();
        Assertions.assertTrue(todoList.getTasks().contains(task), "The remove task action should be undone successfully");
    }

    @Test
    public void testTaskCompletion() {
        Task task = new Task("Finish project", LocalDate.now());
        todoList.addTask(task);
        task.setTaskCompleted(true);
        Assertions.assertTrue(task.isTaskCompleted(), "The task should be marked as completed");
    }

    @Test
    public void testSerializationAndDeserialization() {
        Task task = new Task("Plan holiday", LocalDate.now());
        todoList.addTask(task);
        String filename = "test_todo_list.ser";

        SerializationUtils.serialize(todoList, filename);
        TodoList deserializedList = SerializationUtils.deserialize(filename);

        Assertions.assertNotNull(deserializedList, "The deserialized todo list should not be null");
        Assertions.assertFalse(deserializedList.getTasks().isEmpty(), "The deserialized todo list should contain tasks");
        Assertions.assertEquals(todoList.getTasks().get(0).getTaskName(), deserializedList.getTasks().get(0).getTaskName(), "The task names should match after deserialization");
    }

    @Test
    public void testToggleHighlightCommand() {
        Task task = new Task("Learn piano", LocalDate.now(), false);
        todoList.addTask(task);
        Command highlightCommand = new toggleHighlightCommand(task);
        todoList.performCommand(highlightCommand);
        Assertions.assertTrue(task.isImportantHighlight(), "The task should be marked as important/highlighted");
    }

    @Test
    public void testChangeNameCommand() {
        Task task = new Task("Exercise", LocalDate.now());
        todoList.addTask(task);
        String newName = "Exercise daily";
        Command changeName = new changeNameCommand(todoList, task, newName);
        todoList.performCommand(changeName);
        Assertions.assertEquals(newName, task.getTaskName(), "The task name should be changed successfully");
    }


    @Test
    public void testHighlightPersistence() {
        Task task = new Task("Permanent record", LocalDate.now(), true);
        todoList.addTask(task);
        String filename = "test_highlight_persistence.ser";
        SerializationUtils.serialize(todoList, filename);
        TodoList deserializedList = SerializationUtils.deserialize(filename);
        Task deserializedTask = deserializedList.getTasks().get(0);
        Assertions.assertTrue(deserializedTask.isImportantHighlight(), "Task highlight should persist through serialization and deserialization");
    }

    @Test
    public void testChangeDueDateCommand() {
        Task task = new Task("Change due date test", LocalDate.now());
        LocalDate newDate = LocalDate.now().plusDays(5);
        Command changeDateCommand = new changeDateCommand(todoList, task, newDate);
        todoList.performCommand(changeDateCommand);
        Assertions.assertEquals(newDate, task.getDueDate(), "Due date should be updated successfully");
    }

    @Test
    public void testUndoChangeDueDateCommand() {
        Task task = new Task("Undo change due date", LocalDate.now());
        LocalDate originalDate = task.getDueDate();
        LocalDate newDate = LocalDate.now().plusDays(5);
        Command changeDateCommand = new changeDateCommand(todoList, task, newDate);
        todoList.performCommand(changeDateCommand);
        todoList.undoLastAction();
        Assertions.assertEquals(originalDate, task.getDueDate(), "Undo should revert to the original due date");
    }

    @Test
    public void testInvalidTaskAddition() {
        Task invalidTask = new Task("", LocalDate.now()); // Assuming tasks must have a non-empty name
        todoList.addTask(invalidTask);
        Assertions.assertFalse(todoList.getTasks().contains(invalidTask), "Tasks with invalid names should not be added");
    }



}
