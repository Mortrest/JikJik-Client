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
import sample.Models.Tweet;
import sample.Models.Tweets;
import sample.Models.User;
import sample.Models.Users;
import sample.utils.ChangeScene;
import sample.utils.TweetLoad;

import java.io.IOException;

public class eachTweetController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label tweetLabel;

    @FXML
    public GridPane grid;
    @FXML
    private TextArea textArea;
    @FXML
    private Circle profilePicture;
    @FXML
    private Button backBtn;
    @FXML
    private TextArea overlayText;
    @FXML
    private Pane overlay;


    public void initialize() throws IOException {
        if (Tweets.getTweetID() != null) {
            Tweet tweet = Tweets.search(Tweets.getTweetID());
            User owner = Users.searchUsername(tweet.getOwner());
            nameLabel.setText("@" + owner.getUsername() + " - " + owner.getName());
            tweetLabel.setText(tweet.getText());
            if (owner.getProfilePic()!= null) {
                profilePicture.setFill(new ImagePattern(new Image(owner.getProfilePic())));
            }
            loadData();
        }
    }

    public void loadData() throws IOException {
        new TweetLoad(grid,textArea,2,overlay,2).load();
    }


    public void back() throws IOException {
        if (Tweets.search(Tweets.getTweetID()).getParent().equals("0")){
            Tweets.setTweetID(null);
        } else {
            Tweets.setTweetID(Tweets.search(Tweets.getTweetID()).getParent());
        }
        new ChangeScene("../FXML/sample.fxml",grid);
    }

    public void closeOverlay(){
        overlay.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (overlayText.getText() != null){
            overlay.setVisible(false);
            Tweets.makeTweet(overlayText.getText(),Tweets.getComment(),Users.getCurrentUser().getUsername(),Users.getCurrentUser().getFollowers());
        }
    }
}
