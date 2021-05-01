
package sample.Controllers;

        import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
        import javafx.fxml.FXML;
        import javafx.scene.control.Label;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.Pane;
        import javafx.scene.shape.Circle;
        import sample.Models.Tweets;
        import sample.Models.Users;
        import java.io.IOException;

public class TweetComponentImageController {
    @FXML
    public FontAwesomeIconView like,liked;
    @FXML
    public FontAwesomeIconView ret,comment,forward,report,block,mute;
    @FXML
    private ImageView imageView;
    public Pane getIdPane() {
        return idPane;
    }
    @FXML
    private Label likeCount,commentCount;
    @FXML
    private Pane idPane;
    @FXML
    private Circle profilePic;
    @FXML
    private Label nameLabel;

    @FXML
    private Label tweetLabel;

    private String TweetID;

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
        Tweets.likeTweet(Users.getCurrentUser(),Tweets.search(TweetID));
    }

    public Circle getProfilePic() {
        return profilePic;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
