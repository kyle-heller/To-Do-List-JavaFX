import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * The main GUI application class for managing a todo list. It allows users to add, remove, mark tasks as completed,
 * highlight important tasks, and undo actions.
 */
public class TodoListGUI extends Application {

    private static TodoList todoList; // The main todo list model for the application.
    private ObservableList<Task> taskListObservable; // Observable list for JavaFX list view binding.
    private ListView<Task> taskListView; // ListView for displaying tasks.

    @Override
    public void start(Stage primaryStage) {
        todoList = new TodoList();
        taskListObservable = FXCollections.observableArrayList(todoList.getTasks());
        taskListView = new ListView<>(taskListObservable);

        Label titleLabel = new Label(" To-Do List");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        Button addTaskButton = new Button("+");
        addTaskButton.setOnAction(event -> showAddTaskDialog());

        Button undoButton = new Button("Undo");
        undoButton.setOnAction(event -> {
            todoList.undoLastAction();
            taskListObservable.setAll(todoList.getTasks());
        });

        ComboBox<String> sortOptions = new ComboBox<>();
        sortOptions.getItems().addAll("Sort by Due Date", "Sort by Date Added");
        sortOptions.setValue("Sort by Date Added"); // Default sorting option

        sortOptions.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            switch (newValue) {
                case "Sort by Due Date":
                    sortByDueDate();
                    break;
                case "Sort by Date Added":
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

        taskListView.setCellFactory(lv -> new ListCell<Task>() {
            private final CheckBox checkBox = new CheckBox();
            private final Button optionsButton = new Button("...");

            @Override
            public void updateItem(Task item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);
                setStyle("");
                if (empty || item == null) {
                    // Clean up the cell if it is empty or the item is null
                } else {
                    // Populate the cell if it contains a Task item
                    checkBox.setText(item.getTaskName() + " - Due: " + item.getDueDateString());
                    checkBox.setSelected(item.isTaskCompleted());
                    checkBox.setOnAction(e -> {
                        item.setTaskCompleted(checkBox.isSelected());
                        updateAndSortTaskList(); // Sort and update list when a task's completion status changes
                    });
                    setTextFill(item.isTaskCompleted() ? Color.GRAY : Color.BLACK); // Grey out if completed

                    if (item.isImportantHighlight()) {
                        setStyle("-fx-background-color: #FFD700;"); // Set background color to yellow for important tasks
                    } else {
                        setStyle(""); // Clear any previous styling
                    }

                    optionsButton.setStyle("-fx-padding: 2px 5px;");
                    optionsButton.setOnAction(event -> {
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem deleteItem = new MenuItem("Delete");
                        deleteItem.setOnAction(deleteEvent -> {
                            taskListObservable.remove(item);
                            todoList.removeTask(item.getTaskName());
                        });

                        MenuItem highlightItem = new MenuItem("Mark Important");
                        highlightItem.setOnAction(highlightEvent -> {
                            toggleHighlightCommand highlightCommand = new toggleHighlightCommand(item);
                            todoList.performCommand(highlightCommand);
                            updateItem(item, empty); // Refresh cell appearance
                        });

                        MenuItem changeNameItem = new MenuItem("Change Name");
                        changeNameItem.setOnAction(changeNameEvent -> {
                            TextInputDialog dialog = new TextInputDialog(item.getTaskName());
                            dialog.setTitle("Change Task Name");
                            dialog.setHeaderText("Enter new task name:");
                            Optional<String> result = dialog.showAndWait();
                            result.ifPresent(newName -> {
                                changeNameCommand changeNameCommand = new changeNameCommand(todoList, item, newName);
                                todoList.performCommand(changeNameCommand);
                                updateItem(item, empty); // Refresh cell appearance
                            });
                        });

                        MenuItem changeDateItem = new MenuItem("Change Date");
                        changeDateItem.setOnAction(changeDateEvent -> {
                            TextInputDialog dialog = new TextInputDialog(item.getDueDateString());
                            dialog.setTitle("Change Due Date");
                            dialog.setHeaderText("Enter new due date (YYYY-MM-DD), or leave blank to remove:");
                            Optional<String> result = dialog.showAndWait();
                            result.ifPresent(newDateString -> {
                                LocalDate newDueDate = null;
                                try {
                                    newDueDate = LocalDate.parse(newDateString);
                                } catch (DateTimeParseException e) {
                                    System.err.println("Invalid date format.");
                                    return;
                                }
                                changeDateCommand changeDateCommand = new changeDateCommand(todoList, item, newDueDate);
                                todoList.performCommand(changeDateCommand);
                                updateItem(item, empty); // Refresh cell appearance
                            });
                        });

                        contextMenu.getItems().addAll(deleteItem, highlightItem, changeNameItem, changeDateItem);
                        contextMenu.show(optionsButton, Side.BOTTOM, 0, 0);
                    });

                    HBox hbox = new HBox(checkBox, optionsButton);
                    hbox.setSpacing(5);
                    setGraphic(hbox);
                }
            }
        });

        Scene scene = new Scene(mainLayout, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("To-Do List");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> todoList.saveTodoList());
    }

    private void sortByDueDate() {
        FXCollections.sort(taskListObservable, Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
        updateListView();
    }

    private void sortByLatestAdded() {
        FXCollections.sort(taskListObservable, Comparator.comparingInt(Task::getTaskID));
        updateListView();
    }

    private void updateListView() {
        taskListView.setItems(null); // Force the ListView to update
        taskListView.setItems(taskListObservable); // Re-set the sorted items
    }

    private void updateAndSortTaskList() {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // Introduce a small delay to ensure UI updates smoothly
        pause.setOnFinished(event -> {
            FXCollections.sort(taskListObservable, Comparator.comparing(Task::isTaskCompleted).thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
            updateListView(); // Refresh the list view to display the sorted tasks
        });
        pause.play();
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
            addTaskCommand addTaskCommand = new addTaskCommand(todoList, newTask);
            todoList.performCommand(addTaskCommand);
            taskListObservable.add(newTask); // Adding directly into observable list for immediate UI update
        });
    }

    public static void setTodoList(TodoList todoList) {
        TodoListGUI.todoList = todoList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
