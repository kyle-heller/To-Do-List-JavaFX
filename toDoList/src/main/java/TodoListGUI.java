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
import javafx.util.StringConverter;
import java.util.Comparator;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class TodoListGUI extends Application {

    private static TodoList todoList;
    private ObservableList<Task> taskListObservable;
    private ListView<Task> taskListView;

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

        taskListView.setCellFactory(lv -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();
            private final Button optionsButton = new Button("...");

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

                    if (item.isImportantHighlight()) {
                        setStyle("-fx-background-color: #FFD700;"); // Set background color to yellow for important tasks
                    } else {
                        setStyle(""); // Clear any previous styling
                    }

                    // Options button
                    optionsButton.setStyle("-fx-padding: 2px 5px;");
                    optionsButton.setOnAction(event -> {
                        // Create context menu
                        ContextMenu contextMenu = new ContextMenu();

                        // Delete option
                        MenuItem deleteItem = new MenuItem("Delete");
                        deleteItem.setOnAction(deleteEvent -> {
                            taskListObservable.remove(item);
                            todoList.removeTask(item.getTaskName());
                        });

                        // Highlight option
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
                                // Create a ChangeNameCommand and execute it
                                changeNameCommand changeNameCommand = new changeNameCommand(todoList, item, newName);
                                todoList.performCommand(changeNameCommand);
                                updateItem(item, empty); // Refresh cell appearance
                            });
                        });
                        MenuItem changeDateItem = new MenuItem("Change Date");
                        changeDateItem.setOnAction(changeDateEvent -> {
                            // Show a dialog to enter a new due date
                            TextInputDialog dialog = new TextInputDialog(item.getDueDateString());
                            dialog.setTitle("Change Due Date");
                            dialog.setHeaderText("Enter new due date (YYYY-MM-DD), or leave blank to remove:");
                            Optional<String> result = dialog.showAndWait();
                            result.ifPresent(newDateString -> {
                                LocalDate newDueDate = null;
                                if (!newDateString.isEmpty()) {
                                    try {
                                        newDueDate = LocalDate.parse(newDateString);
                                    } catch (DateTimeParseException e) {
                                        System.err.println("Invalid date format.");
                                    }
                                }
                                // Create a changeDateCommand and execute it
                                changeDateCommand changeDateCommand = new changeDateCommand(todoList, item, newDueDate);
                                todoList.performCommand(changeDateCommand);
                                // Update the UI
                                updateItem(item, empty);
                            });
                        });



                        // Add items to context menu
                        contextMenu.getItems().addAll(deleteItem, highlightItem, changeNameItem, changeDateItem);                        contextMenu.show(optionsButton, Side.BOTTOM, 0, 0);
                    });

                    // Add checkbox and options button to HBox
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

        primaryStage.setOnCloseRequest(event -> {
            todoList.saveTodoList();
        });

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
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5)); // 0.5 seconds delay
        pause.setOnFinished(event -> {
            FXCollections.sort(taskListObservable, Comparator.comparing(Task::isTaskCompleted).thenComparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder())));
            taskListView.setItems(null);
            taskListView.setItems(taskListObservable);
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
            Command addTaskCommand = new addTaskCommand(todoList, newTask);
            todoList.performCommand(addTaskCommand);
            taskListObservable.add(newTask); //adding directly into observable list

        });
    }

    public static void setTodoList(TodoList todoList) {
        TodoListGUI.todoList = todoList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
