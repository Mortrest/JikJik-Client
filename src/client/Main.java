package client;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
//        Platform.runLater(()-> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("FXML/SignIn.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle("Jik Jik");
            primaryStage.getIcons().add(new Image("/client/FXML/download.jpg"));
            primaryStage.setScene(new Scene(root, 1069, 673));
            primaryStage.show();
//        });
    }
    public static void main(String[] args) throws IOException {
        Manager manager = new Manager();
        launch(args);
    }
}
