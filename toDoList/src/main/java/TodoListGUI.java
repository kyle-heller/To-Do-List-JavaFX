import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.util.Comparator;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javafx.scene.paint.Color;
import javafx.scene.control.ListCell;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class TodoListGUI extends Application {

    private TodoList todoList;
    private ObservableList<Task> taskListObservable;
    private ListView<Task> taskListView; // Moved to be a class member

    @Override
    public void start(Stage primaryStage) {
        todoList = new TodoList();
        taskListObservable = FXCollections.observableArrayList(todoList.getTasks());
        taskListView = new ListView<>(taskListObservable); // Initialization moved here

        primaryStage.setTitle("To-Do List");

        // Custom cell factory for ListView
        taskListView.setCellFactory(lv -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            public void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    checkBox.setText(item.getTaskName() + " - Due: " + item.getDueDateString());
                    checkBox.setSelected(item.isTaskCompleted());
                    checkBox.setOnAction(e -> {
                        item.setTaskCompleted(checkBox.isSelected());
                        updateAndSortTaskList(); // Sort and update list when a task's completion status changes
                    });
                    setTextFill(item.isTaskCompleted() ? Color.GRAY : Color.BLACK); // Grey out if completed
                    setGraphic(checkBox);
                }
            }
        });

        Button addTaskButton = new Button("+");
        addTaskButton.setOnAction(event -> showAddTaskDialog());

        HBox topPane = new HBox(addTaskButton);
        topPane.setAlignment(Pos.TOP_RIGHT);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topPane);
        mainLayout.setCenter(taskListView);

        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Comparator<Task> taskComparator = (task1, task2) -> {
        boolean completed1 = task1.isTaskCompleted();
        boolean completed2 = task2.isTaskCompleted();
        if (completed1 == completed2) return 0;
        return completed1 ? 1 : -1;
    };

    private void updateAndSortTaskList() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // 0.5 seconds delay
        pause.setOnFinished(event -> {
            FXCollections.sort(taskListObservable, taskComparator); // Sort the list
            taskListView.setItems(null); // Force the ListView to update
            taskListView.setItems(taskListObservable); // Re-set the sorted items
        });
        pause.play(); // Start the pause
    }

    private void showAddTaskDialog() {
        TextInputDialog taskNameDialog = new TextInputDialog();
        taskNameDialog.setTitle("Add New Task");
        taskNameDialog.setHeaderText("Create a new task");
        taskNameDialog.setContentText("Task name:");
        Optional<String> nameResult = taskNameDialog.showAndWait();

        nameResult.ifPresent(name -> {
            TextInputDialog dueDateDialog = new TextInputDialog();
            dueDateDialog.setTitle("Due Date");
            dueDateDialog.setHeaderText("Enter due date (YYYY-MM-DD), or leave blank for no due date:");
            dueDateDialog.setContentText("Due date:");
            Optional<String> dueDateResult = dueDateDialog.showAndWait();

            LocalDate dueDate = null;
            if (dueDateResult.isPresent() && !dueDateResult.get().isEmpty()) {
                try {
                    dueDate = LocalDate.parse(dueDateResult.get());
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format, task added without a due date.");
                }
            }

            Task newTask = new Task(name, dueDate);
            todoList.addTask(newTask);
            taskListObservable.add(newTask); // Directly add to observable list
            updateAndSortTaskList(); // Sort and update list after adding a task
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
