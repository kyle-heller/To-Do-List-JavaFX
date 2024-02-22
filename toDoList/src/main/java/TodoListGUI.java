import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TodoListGUI extends Application {

    private ListView<Task> taskListView;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the main layout
        BorderPane mainLayout = new BorderPane();

        // Create the ListView for tasks
        taskListView = new ListView<>();
        // TODO: Add your Task objects to the taskListView here

        // Create the add task button
        Button addTaskButton = new Button("+");
        addTaskButton.setOnAction(event -> {
            // TODO: Implement the add task functionality
        });

        // Set the main layout's children
        mainLayout.setTop(addTaskButton); // Add task button at the top
        mainLayout.setCenter(taskListView); // List view in the center

        // Create the scene and show the stage
        Scene scene = new Scene(mainLayout, 400, 600); // Set the window size
        primaryStage.setTitle("To-Do List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
