import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TodoListTest {

    private TodoList todoList;

    @BeforeEach
    public void setUp() {
        todoList = new TodoList();
    }

    @Test
    public void testAddTask() {
        String taskName = "Learn Java";
        todoList.addTask(taskName);
        Assertions.assertTrue(todoList.containsTask(taskName), "The task should be added successfully");
    }
}
