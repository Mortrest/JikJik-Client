package client.Controllers;

import client.Models.Notif;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class NotificationComponentController {

    Notif notif;
    @FXML
    private Pane TweetPane111;

    @FXML
    private Label name;
    @FXML
    private Circle profilePicture;
    @FXML
    private Label text;

    @FXML
    private Button accept, mute;

    @FXML
    private Button decline;

    public Notif getNotif() {
        return notif;
    }

    public void setNotif(Notif notif) {
        this.notif = notif;
    }

    public Button getAccept() {
        return accept;
    }

    public Button getMute() {
        return mute;
    }

    public Button getDecline() {
        return decline;
    }

    public void setName(String str) {
        name.setText(str);
    }

    public void setText(String str) {
        text.setText(str);
    }

    public void hide() {
        accept.setVisible(false);
        decline.setVisible(false);
        mute.setVisible(false);
    }

    public Circle getProfilePicture() {
        return profilePicture;
    }
}
