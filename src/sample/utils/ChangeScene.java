package sample.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChangeScene {
    String url;
    GridPane gridPane;

    public ChangeScene(String url,GridPane gridPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(url));
        Stage window = (Stage) gridPane.getScene().getWindow();
        window.setScene(new Scene(root));

    }
}
