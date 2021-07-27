package client.Controllers;

import client.Manager;
import client.network.ClientManager;
import client.shared.MakeTweetResponse;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import client.Config;
import client.Models.Tweet;
import client.Models.User;
import client.utils.ChangeScene;
import client.utils.TweetLoad;
import java.io.IOException;

public class EachTweetController {
    Gson gson;
    ClientManager clientManager;
    User currentUser;
    public Pane overlay1;
    public GridPane overlayGrid;
    public Button sendMsg;
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
    private TextArea overlayText;
    @FXML
    private Pane overlay;

    public void initialize() throws IOException {
        this.clientManager = Manager.getClientManager();
        this.currentUser = Manager.getUser();
        this.gson = new Gson();
        clientManager.sendTweets("GET_TWEET_ID");
        String ID = clientManager.read();
        if (!ID.equals("NULL")) {
            clientManager.sendTweets("SEARCH_TWEET");
            clientManager.sendClicked(ID);
            Tweet tweet = gson.fromJson(clientManager.read(),Tweet.class);
            assert tweet != null;
            User owner = Manager.getUser(tweet.getOwner());
            assert owner != null;
            nameLabel.setText("@" + owner.getUsername() + " - " + owner.getName());
            tweetLabel.setText(tweet.getText());
            if (owner.getProfilePic() != null) {
                profilePicture.setFill(new ImagePattern(new Image(owner.getProfilePic())));
            }
            loadData();
        }
    }

    public void loadData() throws IOException {
        new TweetLoad(grid, textArea, 2, overlay, overlay1, overlayGrid, sendMsg, 2, clientManager, currentUser).load();
    }


    public void back() throws IOException {
        clientManager.sendTweets("GET_TWEET_ID");
        String ID = clientManager.read();
        clientManager.sendTweets("SEARCH_TWEET");
        clientManager.sendClicked(ID);
        Tweet tweet = gson.fromJson(clientManager.read(),Tweet.class);
        if (tweet.getParent().equals("0")) {
            clientManager.sendTweets("TWEET_NULL");
        } else {
            clientManager.sendTweets("SET_TWEET_ID");
            clientManager.sendClicked(tweet.getParent());
        }
        new ChangeScene(Config.getConfig("mainConfig").getProperty((String.class), "MainHub"), grid);
    }

    public void closeOverlay() {
        overlay.setVisible(false);
        overlay1.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (overlayText.getText() != null) {
            overlay.setVisible(false);
            clientManager.sendTweets("COMMENT_ID");
            String ID = clientManager.read();
            MakeTweetResponse mk = new MakeTweetResponse(overlayText.getText(), ID, currentUser.getUsername(), currentUser.getFollowers(), null, false);
            clientManager.sendTweets("MAKE_TWEET");
            Gson gson = new Gson();
            clientManager.sendClicked(gson.toJson(mk));
            grid.getChildren().clear();
            loadData();
        }
    }
}
