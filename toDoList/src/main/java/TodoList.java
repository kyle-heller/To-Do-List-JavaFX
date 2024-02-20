import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TodoList extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blank JavaFX Window");
        primaryStage.setScene(new Scene(new StackPane(), 300, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void addTask(String taskName) {
        System.out.println(taskName);
    }

    public boolean containsTask(String taskName) {
        return true;
    }

}