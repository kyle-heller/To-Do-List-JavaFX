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

//    public TodoList(SerlializedData) {
//
//    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To-Do List");

        // Create the ListView for tasks
        ListView<Task> taskListView = new ListView<>(taskList);
        taskListView.setCellFactory(lv -> new CheckBoxListCell<>(
                task -> task.taskCompletedProperty(), // Corrected to use the taskCompleted property
                new StringConverter<Task>() {
                    @Override
                    public String toString(Task task) {
                        return task.getTaskName(); // modify this as needed
                    }

                    @Override
                    public Task fromString(String string) {
                        return null; // no conversion from string needed
                    }
                }
        ));

        // Main layout container
        BorderPane mainLayout = new BorderPane();
        // mainLayout.setTop(addTaskButton); // You need to create and set this button
        mainLayout.setCenter(taskListView);

        // Create the scene and show the stage
        Scene scene = new Scene(mainLayout, 400, 600); // Set the window size
        primaryStage.setScene(scene);
        primaryStage.show(); // Show the stage
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