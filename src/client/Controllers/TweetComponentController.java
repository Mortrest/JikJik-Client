package client.Controllers;

import client.Manager;
import client.Models.Tweet;
import client.Models.User;
import client.network.ClientManager;
import com.google.gson.Gson;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class TweetComponentController {
    @FXML
    public FontAwesomeIconView like,liked;
    @FXML
    public FontAwesomeIconView ret,comment,forward,report,block,mute,block1,mute1,report1;

    @FXML
    private Label likeCount,commentCount;
    @FXML
    private Pane idPane,goToProfile;
    @FXML
    private Circle profilePic;
    @FXML
    private Label nameLabel;

    ClientManager clientManager;
    User currentUser;

    @FXML
    private Label tweetLabel;

    private String TweetID;
    private Gson gson;

    public void setNameLabel(String str){
        nameLabel.setText(str);
    }

    public void setTweetLabel(String str){
        tweetLabel.setText(str);
    }

    public String getTweetID() {
        return TweetID;
    }

    public FontAwesomeIconView getRet() {
        return ret;
    }

    public FontAwesomeIconView getComment() {
        return comment;
    }

    public FontAwesomeIconView getForward() {
        return forward;
    }

    public FontAwesomeIconView getReport() {
        return report;
    }

    public FontAwesomeIconView getBlock() {
        return block;
    }

    public FontAwesomeIconView getMute() {
        return mute;
    }

    public void setTweetID(String tweetID) {
        TweetID = tweetID;
    }

    public Label getLikeCount() {
        return likeCount;
    }

    public Label getCommentCount() {
        return commentCount;
    }

    public void setLikeCount(Label likeCount) {
        this.likeCount = likeCount;
    }

    public Pane getIdPane() {
        return idPane;
    }

    
    public void initialize(){
        this.clientManager = Manager.getClientManager();
        this.currentUser = Manager.getUser();
        this.gson = new Gson();
    }

    public void likeTweet() throws IOException {
        int likeCo = Integer.parseInt(likeCount.getText());
        if (liked.isVisible()){
            liked.setVisible(false);
            likeCo--;
        }else {
            liked.setVisible(true);
            likeCo++;
        }
        likeCount.setText(Integer.toString(likeCo));
        clientManager.sendTweets("LIKE");
        clientManager.sendClicked(TweetID);
//        Tweets.likeTweet(currentUser,Tweets.search(TweetID));
    }



    public Circle getProfilePic() {
        return profilePic;
    }


    public void loadData() throws IOException {
        clientManager.sendTweets("SEARCH_TWEET");
        clientManager.sendClicked(TweetID);
//        Tweet tweet = Tweets.search(TweetID);
        Tweet tweet = gson.fromJson(clientManager.read(),Tweet.class);
        block.setOnMouseClicked(e -> {
            assert tweet != null;
            try {
                clientManager.sendUsers("BLOCK");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                clientManager.sendClicked(tweet.getOwner());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//            Users.blockProfile(currentUser,tweet.getOwner());
            block1.setVisible(!block1.isVisible());

        });
        mute.setOnMouseClicked(e -> {
            assert tweet != null;
            try {
                clientManager.sendUsers("MUTE");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                clientManager.sendClicked(tweet.getOwner());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
//            Users.muteProfile(currentUser,tweet.getOwner());
            mute1.setVisible(!mute1.isVisible());

        });
        report.setOnMouseClicked(e -> {
            try {
                clientManager.sendTweets("REPORT");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                clientManager.sendClicked(tweet.getOwner());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

//            Tweets.reportUser(currentUser,TweetID);
            System.out.println(report1.isVisible());
            report1.setVisible(!report1.isVisible());
                });
    }

    public Pane getGoToProfile() {
        return goToProfile;
    }
}
