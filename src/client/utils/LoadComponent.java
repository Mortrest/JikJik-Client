package client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class LoadComponent {
    AnchorPane anchorPane;
    FXMLLoader fxmlLoader;
    public LoadComponent(String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        this.fxmlLoader = fxmlLoader;
        fxmlLoader.setLocation(getClass().getResource(url));
        this.anchorPane = fxmlLoader.load();
    }

    public AnchorPane loadAnchor(){
        return anchorPane;
    }

    public FXMLLoader loadFxml(){
        return fxmlLoader;
    }

}
