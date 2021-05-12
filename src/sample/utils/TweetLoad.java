package sample.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import sample.Controllers.FollowerComponentController;
import sample.Controllers.TweetComponentController;
import sample.Controllers.TweetComponentImageController;
import sample.Models.*;

import java.io.IOException;
import java.util.LinkedList;

public class TweetLoad {
    @FXML
    GridPane grid;
    TextArea textArea;
    Pane overlay;
    Pane overlay1;
    GridPane overlayGrid;
    Button sendMsg;
    int type;
    int isComment;

    public TweetLoad(GridPane grid, TextArea textArea, int type, Pane overlay, int isComment) {
        this.grid = grid;
        this.type = type;
        this.textArea = textArea;
        this.overlay = overlay;
        this.isComment = isComment;
    }

    public TweetLoad(GridPane grid, TextArea textArea, int type, Pane overlay, Pane overlay1, GridPane overlayGrid, Button sendMsg, int isComment) {
        this.grid = grid;
        this.type = type;
        this.textArea = textArea;
        this.overlay = overlay;
        this.isComment = isComment;
        this.overlay1 = overlay1;
        this.overlayGrid = overlayGrid;
        this.sendMsg = sendMsg;
    }

    // 2 Comment
    public void load() throws IOException {
        if (this.sendMsg != null) {
            sendMsg.setOnAction(e -> sendMsg(Tweets.search(Tweets.getForwardTweetID())));
        }
        LinkedList<Tweet> tw;
        if (isComment == 1) {
            tw = Users.getTweets().showTweetOwnPage(Users.class, Users.getCurrentUser().getUsername(), type);
        } else {
            tw = Users.getTweets().getComments(Tweets.getTweetID());
        }

        for (int i = 1; i < tw.size() + 1; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            if (tw.get(i - 1).getImage() == null) {
                fxmlLoader.setLocation(getClass().getResource("../FXML/TweetComponent.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TweetComponentController itemController = fxmlLoader.getController();
                if (Users.searchUsername(tw.get(i-1).getOwner()).getProfilePic() != null) {
                    itemController.getProfilePic().setFill(new ImagePattern(new Image(Users.searchUsername(tw.get(i - 1).getOwner()).getProfilePic())));
                }
                int finalI = i - 1;
                itemController.setTweetID(tw.get(i - 1).getID());
                itemController.loadData();
                int finalI1 = i;
                itemController.getGoToProfile().setOnMouseClicked(e -> {
                    try {
                        goToProfile(tw.get(finalI1 -1).getOwner(),anchorPane);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getLikeCount().setText(Integer.toString(tw.get(i - 1).getLikes().size()));
                itemController.getRet().setOnMouseClicked(e -> {
                    try {
                        retweet(tw.get(finalI).getID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getComment().setOnMouseClicked(e -> makeTweet(tw.get(finalI).getID()));
                itemController.getForward().setOnMouseClicked(e -> {
                    try {
                        Tweets.setForwardTweetID(tw.get(finalI).getID());
                        loadFlw();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getIdPane().setOnMouseClicked(e -> {
                    try {
                        tweetPage(tw.get(finalI).getID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.setNameLabel("@" + tw.get(i - 1).getOwner() + " - " + Users.searchUsername(tw.get(i - 1).getOwner()).getName());
                String text = tw.get(i - 1).getText();
                if (tw.get(i - 1).isRet()) {
                    text = "[Retweeted] " + text;
                }
                itemController.setTweetLabel(text);
                grid.add(anchorPane, 1, grid.getRowCount() + 1);
                GridPane.setMargin(anchorPane, new Insets(10));

            } else {
                fxmlLoader.setLocation(getClass().getResource("../FXML/TweetComponentImage.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                TweetComponentImageController itemController = fxmlLoader.getController();

                itemController.getProfilePic().setFill(new ImagePattern(new Image(Users.searchUsername(tw.get(i-1).getOwner()).getProfilePic())));
                int finalI = i - 1;
                itemController.setTweetID(tw.get(i - 1).getID());
                itemController.loadData();
                itemController.getLikeCount().setText(Integer.toString(tw.get(i - 1).getLikes().size()));
                itemController.getForward().setOnMouseClicked(e -> {
                    try {
                        Tweets.setForwardTweetID(tw.get(finalI).getID());
                        loadFlw();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                int finalI2 = i;
                itemController.getGoToProfile().setOnMouseClicked(e -> {
                    try {
                        goToProfile(tw.get(finalI2 -1).getOwner(),anchorPane);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getRet().setOnMouseClicked(e -> {
                    try {
                        retweet(tw.get(finalI).getID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getImageView().setImage(new Image(tw.get(i - 1).getImage()));
                itemController.getComment().setOnMouseClicked(e -> makeTweet(tw.get(finalI).getID()));
                itemController.getIdPane().setOnMouseClicked(e -> {
                    try {
                        tweetPage(tw.get(finalI).getID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.setNameLabel("@" + tw.get(i - 1).getOwner() + " - " + Users.searchUsername(tw.get(i - 1).getOwner()).getName());
                itemController.setTweetLabel(tw.get(i - 1).getText());
                grid.add(anchorPane, 1, grid.getRowCount() + 1);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        }
    }

    // Send Messages

    public void sendMsg(Tweet tweet) {
        for (String user : members) {
            Room room1 = Chats.searchRoomID(Chats.searchGroup(user));
            if (room1 != null) {
                Chats.makeChat(tweet.getText(), Users.getCurrentUser().getUsername(), room1.getRoomID());
                if (tweet.getImage() != null) {
                    Chats.makeImageChat(tweet.getText(), Users.getCurrentUser().getUsername(), room1.getRoomID(), tweet.getImage());
                }
            } else {
                Chats.makeChat(tweet.getText(), Users.getCurrentUser().getUsername(), user);
                if (tweet.getImage() != null) {
                    Chats.makeImageChat(tweet.getText(), Users.getCurrentUser().getUsername(), user, tweet.getImage());
                }
            }
        }
        overlay1.setVisible(false);
    }


    public void loadFlw() throws IOException {
        overlay1.setVisible(true);
        overlayGrid.getChildren().clear();
        LinkedList<Room> rooms = Chats.userRoom(Users.getProfile().getUsername());
        LinkedList<String> users;
        users = Users.getCurrentUser().getFollowers();
        for (Room room : rooms) {
            LoadComponent loadComponent = new LoadComponent("../FXML/FollowersComponent.fxml");
            AnchorPane anchorPane = loadComponent.loadAnchor();
            FollowerComponentController item = loadComponent.loadFxml().getController();
            String str;
            if (room.getType().equals("pv")) {
                str = room.getOwner1() + "-" + room.getOwner2();
                item.setName(str);
                str = room.getRoomID();
            } else {
                str = room.getGroupName();
                item.setName(str);
            }

            String finalStr = str;
            item.getPane().setOnMouseClicked(e -> chooseFlwr(item, finalStr));
            overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
            overlayGrid.setLayoutX(-150);
            overlayGrid.setLayoutY(-25);
            GridPane.setMargin(anchorPane, new Insets(-25));

        }
        for (String str : users) {
            LoadComponent loadComponent = new LoadComponent("../FXML/FollowersComponent.fxml");
            AnchorPane anchorPane = loadComponent.loadAnchor();
            FollowerComponentController item = loadComponent.loadFxml().getController();
            item.setName("@" + str);
            item.getPane().setOnMouseClicked(e -> chooseFlwr(item, str));
            overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
            overlayGrid.setLayoutX(-150);
            overlayGrid.setLayoutY(-25);
            GridPane.setMargin(anchorPane, new Insets(-25));

        }
    }

    LinkedList<String> members = new LinkedList<>();

    public void chooseFlwr(FollowerComponentController item, String str) {
        if (item.rect1.isVisible()) {
            members.remove(str);
            item.rect1.setVisible(false);
        } else {
            item.rect1.setVisible(true);
            members.add(str);
        }
    }

    public void makeTweet(String ID) {
        overlay.setVisible(true);
        Tweets.setComment(ID);
    }

    public void tweetPage(String ID) throws IOException {
        Tweets.setTweetID(ID);
        new ChangeScene("../FXML/sample.fxml", grid);

    }

    public void retweet(String ID) throws IOException {
        Tweets.reTweet(Tweets.search(ID), Users.getCurrentUser());
        grid.getChildren().clear();
        load();
    }

    public void goToProfile(String owner,AnchorPane anchorPane) throws IOException {
        Users.setProfile(Users.searchUsername(owner));
        new ChangeScene("../FXML/sample.fxml",anchorPane);

    }
}
