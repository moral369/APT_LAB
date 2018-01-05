package drill.Login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLExample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("fxml_example.fxml"));

        Scene whole_scene = new Scene(root, 300, 275);

        primaryStage.setTitle("APT Dashboard");
        primaryStage.setScene(whole_scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
