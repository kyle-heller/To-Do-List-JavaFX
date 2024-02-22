import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

        Label titleLabel = new Label(" To-Do List");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        Button addTaskButton = new Button("+");
        addTaskButton.setOnAction(event -> showAddTaskDialog());

        Button undoButton = new Button("Undo");
        undoButton.setOnAction(event -> {
            // Placeholder for undo logic
            System.out.println("Undo action triggered");
        });

        ComboBox<String> sortOptions = new ComboBox<>();
        sortOptions.getItems().addAll("Sort by Due Date", "Sort by Latest Added");
        sortOptions.setValue("Sort by Latest Added"); // Default sorting option

        sortOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            switch (newValue) {
                case "Sort by Due Date":
                    sortByDueDate();
                    break;
                case "Sort by Latest Added":
                    sortByLatestAdded();
                    break;
            }
        });

        HBox rightControls = new HBox(5, sortOptions, undoButton, addTaskButton);
        rightControls.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topLayout = new BorderPane();
        topLayout.setLeft(titleLabel);
        topLayout.setRight(rightControls);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topLayout);
        mainLayout.setCenter(taskListView);

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

        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List");
        primaryStage.show();
    }

    private void sortByDueDate() {
        FXCollections.sort(taskListObservable, Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        updateListView();
    }

    private void updateListView() {
        taskListView.setItems(null); // Force the ListView to update
        taskListView.setItems(taskListObservable); // Re-set the sorted items
    }

    private void sortByLatestAdded() {
        FXCollections.sort(taskListObservable, Comparator.comparingInt(Task::getTaskID));
        updateListView();
    }

    private void updateAndSortTaskList() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // 0.5 seconds delay
        pause.setOnFinished(event -> {
            FXCollections.sort(taskListObservable, Comparator.comparing(Task::isTaskCompleted).thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
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
