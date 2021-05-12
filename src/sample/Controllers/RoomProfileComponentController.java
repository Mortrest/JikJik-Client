package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class RoomProfileComponentController {
    @FXML
    private Label name;
    @FXML
    private Pane pane;
    @FXML
    private Label lastMsg;
    @FXML
    private Circle profilePicture;

    public void setNameLabel(String str){
        name.setText(str);
    }
    public void setLastMsg(String str){
        lastMsg.setText(str);
    }

    public Circle getProfilePicture(){
        return profilePicture;
    }

    public Pane getPane() {
        return pane;
    }
}
