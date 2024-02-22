import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.util.ArrayList;

public class TodoList extends Application {

    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    public TodoList() {

    }

    //TODO add serialization to save tasks
//    public TodoList(SerlializedData) {
//
//    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To-Do List");

        // Create the ListView for tasks
        ListView<Task> taskListView = new ListView<>(taskList);
        taskListView.setCellFactory(lv -> new CheckBoxListCell<>(
                task -> task.taskCompletedProperty(), // corrected to match the Task class
                new StringConverter<Task>() {
                    @Override
                    public String toString(Task task) {
                        return task.getTaskName() + (task.isTaskCompleted() ? " (completed)" : ""); // Shows completion status
                    }

                    @Override
                    public Task fromString(String string) {
                        // No need for this method in this context
                        return null;
                    }
                }
        ));

        // Add sample tasks
        addTask("Sample Task 1");
        addTask("Sample Task 2");

        // Create the add task button (but without functionality for now)
        Button addTaskButton = new Button("+");

        // Set the main layout's children
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(addTaskButton);
        mainLayout.setCenter(taskListView);

        // Create the scene and show the stage
        Scene scene = new Scene(mainLayout, 400, 600); // Set the window size
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public void addTask(String taskName) {
        Task task = new Task(taskName);
        taskList.add(task);
    }

    public void removeTask(String taskName) {
        for (int i = 0; i < taskList.size(); i++) {
            Task task = (Task) taskList.get(i);
            if (task.getTaskName().equals(taskName)) {
                taskList.remove(i);
                break;
            }
        }
    }

    public void completeTask(String taskName) {
        for (int i = 0; i < taskList.size(); i++) {
            Task task = (Task) taskList.get(i);
            if (task.getTaskName().equals(taskName)) {
                task.setTaskCompleted(true);
                break;
            }
        }
    }

    public String toString()
    {
        String result = "";

        for (Object task : taskList) {
            result = result + task + "\n";
        }

        return result;
    }




    public static void main(String[] args) {
//        TodoList td1 = new TodoList();
//
//        td1.addTask("LALALA");
//        td1.addTask("LALALA2");
//
//
//        System.out.println(td1);
//
//        td1.removeTask("LALALA");
//        System.out.println(td1);
//        td1.completeTask("LALALA2");
//        System.out.println(td1);


        launch(args);
    }


}