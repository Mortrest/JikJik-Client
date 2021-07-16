package client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    public ChangeScene(String url, AnchorPane gridPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(url));
        Stage window = (Stage) gridPane.getScene().getWindow();
        window.setScene(new Scene(root));
    }
    public ChangeScene(String url, Button gridPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(url));
        Stage window = (Stage) gridPane.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
