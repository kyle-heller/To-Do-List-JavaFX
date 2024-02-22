import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.util.Optional;

public class TodoListGUI extends Application {

    private TodoList todoList;
    private ObservableList<Task> taskListObservable;

    @Override
    public void start(Stage primaryStage) {
        todoList = new TodoList();
        taskListObservable = FXCollections.observableArrayList(todoList.getTasks());

        primaryStage.setTitle("To-Do List");
        ListView<Task> taskListView = new ListView<>(taskListObservable);
        taskListView.setCellFactory(lv -> new CheckBoxListCell<>(
                task -> task.taskCompletedProperty(),
                new StringConverter<>() {
                    @Override
                    public String toString(Task task) {
                        return task.getTaskName() + (task.isTaskCompleted() ? " (completed)" : "");
                    }

                    @Override
                    public Task fromString(String string) {
                        return null;
                    }
                }
        ));

        // Add task button with action to show dialog
        Button addTaskButton = new Button("+");
        addTaskButton.setOnAction(event -> showAddTaskDialog(taskListView));

        // Right align the add task button
        HBox topPane = new HBox(addTaskButton);
        topPane.setAlignment(Pos.TOP_RIGHT);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topPane);
        mainLayout.setCenter(taskListView);

        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAddTaskDialog(ListView<Task> taskListView) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Task");
        dialog.setHeaderText("Create a new task");
        dialog.setContentText("Task name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            todoList.addTask(name);
            taskListObservable.setAll(todoList.getTasks());
        });
    }
    public static void main(String[] args) {
        launch(args);
    }
}
