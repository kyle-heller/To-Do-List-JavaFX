import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class TodoList { //extends Application

    ArrayList taskList = new ArrayList(10);

    public TodoList() {

    }

//    public TodoList(SerlializedData) {
//
//    }

//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Blank JavaFX Window");
//        primaryStage.setScene(new Scene(new StackPane(), 300, 250));
//        primaryStage.show();
//    }


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
        TodoList td1 = new TodoList();

        td1.addTask("LALALA");
        td1.addTask("LALALA2");


        System.out.println(td1);

        td1.removeTask("LALALA");
        System.out.println(td1);
        td1.completeTask("LALALA2");
        System.out.println(td1);


//        launch(args);
    }


}