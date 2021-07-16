//
//package sample.Controllers;
//
//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.Pane;
//import javafx.scene.shape.Circle;
//import sample.Models.Tweet;
//import sample.Logic.Tweets;
//import sample.Logic.Users;
//
//import java.io.IOException;
//
//public class TweetComponentImageController {
//    @FXML
//    public FontAwesomeIconView like,liked;
//    @FXML
//    public FontAwesomeIconView ret,comment,forward,report,block,mute,report1,block1,mute1;
//    @FXML
//    private ImageView imageView;
//    public Pane getIdPane() {
//        return idPane;
//    }
//    @FXML
//    private Label likeCount,commentCount;
//    @FXML
//    private Pane idPane,goToProfile;
//    @FXML
//    private Circle profilePic;
//    @FXML
//    private Label nameLabel;
//
//    @FXML
//    private Label tweetLabel;
//
//    private String TweetID;
//
//    public void setNameLabel(String str){
//        nameLabel.setText(str);
//    }
//
//    public void setTweetLabel(String str){
//        tweetLabel.setText(str);
//    }
//
//    public String getTweetID() {
//        return TweetID;
//    }
//
//    public FontAwesomeIconView getRet() {
//        return ret;
//    }
//
//    public FontAwesomeIconView getComment() {
//        return comment;
//    }
//
//    public FontAwesomeIconView getForward() {
//        return forward;
//    }
//
//    public FontAwesomeIconView getReport() {
//        return report;
//    }
//
//    public FontAwesomeIconView getBlock() {
//        return block;
//    }
//
//    public FontAwesomeIconView getMute() {
//        return mute;
//    }
//
//    public void setTweetID(String tweetID) {
//        TweetID = tweetID;
//    }
//
//    public Label getLikeCount() {
//        return likeCount;
//    }
//
//    public Label getCommentCount() {
//        return commentCount;
//    }
//
//    public void setLikeCount(Label likeCount) {
//        this.likeCount = likeCount;
//    }
//
//    public void likeTweet() throws IOException {
//        int likeCo = Integer.parseInt(likeCount.getText());
//        if (liked.isVisible()){
//            liked.setVisible(false);
//            likeCo--;
//        }else {
//            liked.setVisible(true);
//            likeCo++;
//        }
//        likeCount.setText(Integer.toString(likeCo));
//        Tweets.likeTweet(Users.getCurrentUser(),Tweets.search(TweetID));
//    }
//
//    public Circle getProfilePic() {
//        return profilePic;
//    }
//
//    public ImageView getImageView() {
//        return imageView;
//    }
//
//
//    public void loadData(){
//        Tweet tweet = Tweets.search(TweetID);
//        block.setOnMouseClicked(e -> {
//            assert tweet != null;
//            Users.blockProfile(Users.getCurrentUser(),tweet.getOwner());
//            block1.setVisible(!block1.isVisible());
//
//        });
//        mute.setOnMouseClicked(e -> {
//            assert tweet != null;
//            Users.muteProfile(Users.getCurrentUser(),tweet.getOwner());
//            mute1.setVisible(!mute1.isVisible());
//
//        });
//        report.setOnMouseClicked(e -> {
//            Tweets.reportUser(Users.getCurrentUser(),TweetID);
//            System.out.println(report1.isVisible());
//            report1.setVisible(!report1.isVisible());
//        });
//    }
//
//    public void setImageView(ImageView imageView) {
//        this.imageView = imageView;
//    }
//
//    public Pane getGoToProfile() {
//        return goToProfile;
//    }
//}
