
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * @author Tianyu Chen Fengge Chen
 */
public class Photos extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        primaryStage.setScene(new Scene(root, 631,428));
        primaryStage.setTitle("Album");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}