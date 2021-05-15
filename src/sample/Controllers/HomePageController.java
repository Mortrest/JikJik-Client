package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import sample.Models.Tweets;
import sample.Models.Users;
import sample.utils.ChangeProfilePicture;
import sample.utils.TweetLoad;

import java.io.File;
import java.io.IOException;

public class HomePageController {
    @FXML
    public GridPane grid;
    @FXML
    public Label nameLabel;
    @FXML
    public Circle proPic;
    public Pane overlay1;
    public GridPane overlayGrid;
    @FXML
    public Button sendMsg;
    @FXML
    private TextArea textArea, overlayText;
    @FXML
    private Pane overlay;

    public void initialize() throws IOException {
        if (Users.getCurrentUser().getProfilePic() != null) {
            Image image = new Image(Users.getCurrentUser().getProfilePic());
            proPic.setFill(new ImagePattern(image));
        }
        nameLabel.setText("@" + Users.getCurrentUser().getUsername() + " - " + Users.getCurrentUser().getName());
        loadData();
    }

    public void addTweet() throws IOException {
        if (Tweets.getImage() == null) {
            Tweets.makeTweet(textArea.getText(), "0", Users.getCurrentUser().getUsername(), Users.getCurrentUser().getFollowers());
            grid.getChildren().clear();
            loadData();
            textArea.setText("");
        } else {
            File file = new File(Tweets.getImage());
            Image image = new Image(file.toURI().toString());
            Tweets.makeTweetImage(textArea.getText(), image.getUrl(), "0", Users.getCurrentUser().getUsername(), Users.getCurrentUser().getFollowers());
            grid.getChildren().clear();
            loadData();
            textArea.setText("");
        }
    }

    public void attach() throws IOException {
        new ChangeProfilePicture(textArea,1);
    }

    public void loadData() throws IOException {
        new TweetLoad(grid, textArea, 1, overlay,overlay1,overlayGrid,sendMsg,1).load();
    }

    public void closeOverlay() {
        overlay.setVisible(false);
        overlay1.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (overlayText.getText() != null) {
            overlay.setVisible(false);
            Tweets.makeTweet(overlayText.getText(), Tweets.getComment(), Users.getCurrentUser().getUsername(), Users.getCurrentUser().getFollowers());
            loadData();
        }
    }


}


