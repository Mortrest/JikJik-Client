package client.shared;

import client.Config;
import client.Controllers.FollowerComponentController;
import client.Controllers.TweetComponentController;
import client.Controllers.TweetComponentImageController;
import client.Models.Room;
import client.Models.Tweet;
import client.Models.User;
import client.network.ClientManager;
import client.utils.ChangeScene;
import client.utils.LoadComponent;
import client.utils.Manager;
import com.google.gson.Gson;
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

import java.io.IOException;
import java.util.LinkedList;

public class TweetLoad {
    ClientManager clientManager;
    User currentUser;
    Gson gson;
    @FXML
    GridPane grid;
    TextArea textArea;
    Pane overlay;
    Pane overlay1;
    GridPane overlayGrid;
    Button send;
    int type;
    int isComment;
//    public TweetLoad(GridPane grid, TextArea textArea, int type, Pane overlay, int isComment,ClientManager clientManager,User currentUser) {
//        this.clientManager = clientManager;
//        this.currentUser = currentUser;
//        this.grid = grid;
//        this.gson = new Gson();
//        this.type = type;
//        this.textArea = textArea;
//        this.overlay = overlay;
//        this.isComment = isComment;
//    }

    public TweetLoad(GridPane grid, TextArea textArea, int type, Pane overlay, Pane overlay1, GridPane overlayGrid, Button send, int isComment,ClientManager clientManager,User currentUser) {
        this.grid = grid;
        this.type = type;
        this.gson = new Gson();
        this.clientManager = clientManager;
        this.currentUser = currentUser;
        this.textArea = textArea;
        this.overlay = overlay;
        this.isComment = isComment;
        this.overlay1 = overlay1;
        this.overlayGrid = overlayGrid;
        this.send = send;
    }

    // 2 Comment
    public void load() throws IOException {
        grid.getChildren().clear();
        if (clientManager.getConnected()) {
            clientManager.sendTweets("GET_FORWARD_ID");
            String a = clientManager.read();
            if (!a.equals("NULL")) {
                clientManager.sendTweets("GET_FORWARD_ID");
                String ID = clientManager.read();
                clientManager.sendTweets("SEARCH_TWEET");
                clientManager.sendClicked(ID);
                Tweet tws = gson.fromJson(clientManager.read(), Tweet.class);
                send.setOnAction(e -> {
                    try {
                        sendMsg(tws);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            }
        }
        LinkedList<Tweet> tw;
        tw = Manager.getHomeTweets();
        if (isComment == 1) {
            if (type == 4) {
                tw = Manager.getProfileTweets();
            }
            if (type == 3){
                tw = Manager.getExploreTweets();
            }
        } else {
            if (clientManager.getConnected()) {
                clientManager.sendTweets("GET_TWEET_ID");
                String ID = clientManager.read();
                clientManager.sendUsers("GET_COMMENTS");
                clientManager.sendClicked(ID);
                GetTweetsResponse gs = gson.fromJson(clientManager.read(), GetTweetsResponse.class);
                tw = gs.getTweets();
            }
        }
        for (int i = 1; i < tw.size() + 1; i++) {
            if (tw.get(i - 1).getReported() <= 5) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                if (tw.get(i - 1).getImage() == null) {
                    fxmlLoader.setLocation(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "TweetComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    TweetComponentController itemController = fxmlLoader.getController();
                    if (clientManager.getConnected()) {
                        User user = Manager.getUser(tw.get(i - 1).getOwner());
                        itemController.setNameLabel("@" + tw.get(i - 1).getOwner() + " - " + user.getName());
                        if (user.getProfilePic() != null) {
                            itemController.getProfilePic().setFill(new ImagePattern(new Image(user.getProfilePic())));
                        }
                    }
                    int finalI = i - 1;
                    itemController.setTweetID(tw.get(i - 1).getID());
                    itemController.loadData();
                    int finalI1 = i;
                    LinkedList<Tweet> finalTw = tw;
                    itemController.getGoToProfile().setOnMouseClicked(e -> {
                        try {
                            goToProfile(finalTw.get(finalI1 - 1).getOwner(), anchorPane);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    itemController.getLikeCount().setText(Integer.toString(tw.get(i - 1).getLikes().size()));
                    LinkedList<Tweet> finalTw1 = tw;
                    itemController.getRet().setOnMouseClicked(e -> {
                        try {
                            retweet(finalTw1.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    LinkedList<Tweet> finalTw2 = tw;
                    itemController.getComment().setOnMouseClicked(e -> {
                        try {
                            makeTweet(finalTw2.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    LinkedList<Tweet> finalTw8 = tw;
                    itemController.getForward().setOnMouseClicked(e -> {
                        try {
                            clientManager.sendTweets("SET_FORWARD_ID");
                            clientManager.sendClicked(finalTw8.get(finalI).getID());
                            loadFlw();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    LinkedList<Tweet> finalTw3 = tw;
                    itemController.getIdPane().setOnMouseClicked(e -> {
                        try {
                            tweetPage(finalTw3.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    String text = tw.get(i - 1).getText();
                    if (tw.get(i - 1).isRet()) {
                        text = "[Retweeted] " + text;
                    }
                    itemController.setTweetLabel(text);
                    grid.add(anchorPane, 1, grid.getRowCount() + 1);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } else {
                    fxmlLoader.setLocation(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "TweetComponentImage")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    TweetComponentImageController itemController = fxmlLoader.getController();
                    int finalI = i - 1;
                    itemController.setTweetID(tw.get(i - 1).getID());
                    itemController.loadData();
                    itemController.getLikeCount().setText(Integer.toString(tw.get(i - 1).getLikes().size()));

                    if (clientManager.getConnected()) {
                        User user = Manager.getUser(tw.get(i - 1).getOwner());
                        itemController.getProfilePic().setFill(new ImagePattern(new Image(user.getProfilePic())));
                        itemController.setNameLabel("@" + tw.get(i - 1).getOwner() + " - " + user.getName());
                        LinkedList<Tweet> finalTw4 = tw;
                        itemController.getForward().setOnMouseClicked(e -> {
                            try {
                                clientManager.sendTweets("SET_FORWARD_ID");
                                clientManager.sendClicked(finalTw4.get(finalI).getID());
//                            Tweets.setForwardTweetID(tw.get(finalI).getID());
                                loadFlw();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        });
                    }
                    int finalI2 = i;
                    LinkedList<Tweet> finalTw5 = tw;
                    itemController.getGoToProfile().setOnMouseClicked(e -> {
                        try {
                            goToProfile(finalTw5.get(finalI2 - 1).getOwner(), anchorPane);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    LinkedList<Tweet> finalTw6 = tw;
                    itemController.getRet().setOnMouseClicked(e -> {
                        try {
                            retweet(finalTw6.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    itemController.getImageView().setImage(new Image(tw.get(i - 1).getImage()));
                    LinkedList<Tweet> finalTw7 = tw;
                    itemController.getComment().setOnMouseClicked(e -> {
                        try {
                            makeTweet(finalTw7.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    LinkedList<Tweet> finalTw9 = tw;
                    itemController.getIdPane().setOnMouseClicked(e -> {
                        try {
                            tweetPage(finalTw9.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    itemController.setTweetLabel(tw.get(i - 1).getText());
                    grid.add(anchorPane, 1, grid.getRowCount() + 1);
                    GridPane.setMargin(anchorPane, new Insets(10));
                } } } }
    // Send Messages

    public void sendMsg(Tweet tweet) throws IOException {
        if (clientManager.getConnected()) {
            for (String user : members) {
                clientManager.sendChats("SEARCH_GROUP");
                clientManager.sendClicked(user);
                String roomID = clientManager.read();
                clientManager.sendChats("SEARCH_ROOM");
                clientManager.sendClicked(roomID);
                Room room1 = gson.fromJson(clientManager.read(), Room.class);
                System.out.println("1");
                if (room1 != null) {
                    MakeChatResponse mk = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room1.getRoomID(), null, false, false);
                    clientManager.sendChats("MAKE_CHAT");
                    clientManager.sendClicked(gson.toJson(mk));
                    if (tweet.getImage() != null) {
                        MakeChatResponse mks = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room1.getRoomID(), tweet.getImage(), true, false);
                        clientManager.sendChats("MAKE_CHAT");
                        clientManager.sendClicked(gson.toJson(mks));
                    }
                } else {
                    System.out.println("2");
                    clientManager.sendChats("SEARCH_ROOM2");
                    clientManager.sendClicked(user);
                    String str = clientManager.read();
                    if (str.equals("NULL")) {
                        clientManager.sendChats("MAKE_ROOM");
                        clientManager.sendClicked(user);
                    }
                    Room room = gson.fromJson(str, Room.class);
//                if (room3 == null) {
//                }
//                clientManager.sendChats("SEARCH_ROOM2");
//                clientManager.sendClicked(user);
                    System.out.println("4");
//                Room room = gson.fromJson(clientManager.read(), Room.class);
                    MakeChatResponse mk = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room.getRoomID(), null, false, false);
                    clientManager.sendChats("MAKE_CHAT");
                    clientManager.sendClicked(gson.toJson(mk));
                    if (tweet.getImage() != null) {
                        MakeChatResponse mks = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room.getRoomID(), tweet.getImage(), true, false);
                        clientManager.sendChats("MAKE_CHAT");
                        clientManager.sendClicked(gson.toJson(mks));
                    }
                }
            }
            overlay1.setVisible(false);
        }
    }

    public void loadFlw() throws IOException {
        if (clientManager.getConnected()) {
            overlay1.setVisible(true);
            overlayGrid.getChildren().clear();
//        clientManager.sendUsers("GET_PROFILE");
//        User us = gson.fromJson(clientManager.read(), User.class);
            clientManager.sendClicked("MSG");
            clientManager.sendClicked("DATA");
            // Inja momkene moshkel dashte bashim

            RoomResponse rs = gson.fromJson(clientManager.read(), RoomResponse.class);
            LinkedList<Room> rooms = rs.getRooms();
            LinkedList<String> users;
            users = currentUser.getFollowers();
            for (Room room : rooms) {
                LoadComponent loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "FollowersComponent"));
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
                item.getPane().setOnMouseClicked(e -> {
                    try {
                        chooseFlwr(item, finalStr);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
                overlayGrid.setLayoutX(-150);
                overlayGrid.setLayoutY(-25);
                GridPane.setMargin(anchorPane, new Insets(-25));
            }
            for (String str : users) {
                LoadComponent loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "FollowersComponent"));
                AnchorPane anchorPane = loadComponent.loadAnchor();
                FollowerComponentController item = loadComponent.loadFxml().getController();
                item.setName("@" + str);
                item.getPane().setOnMouseClicked(e -> {
                    try {
                        chooseFlwr(item, str);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
                overlayGrid.setLayoutX(-150);
                overlayGrid.setLayoutY(-25);
                GridPane.setMargin(anchorPane, new Insets(-25));
            }
            LinkedList<LinkedList<String>> catg = currentUser.getCategories();
            for (int i = 0; i < catg.size(); i++) {
                LoadComponent loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "FollowersComponent"));
                AnchorPane anchorPane = loadComponent.loadAnchor();
                FollowerComponentController item = loadComponent.loadFxml().getController();
                item.setName("Categories: " + catg.get(i).get(0));
                catg.get(i).remove(0);
                int finalI = i;
                item.getPane().setOnMouseClicked(e -> chooseCatg(item, catg.get(finalI)));
                overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
                overlayGrid.setLayoutX(-150);
                overlayGrid.setLayoutY(-25);
                GridPane.setMargin(anchorPane, new Insets(-25));
            }
        }
    }

    LinkedList<String> members = new LinkedList<>();

    public void chooseFlwr(FollowerComponentController item, String str) throws IOException {
        if (clientManager.getConnected()) {
            if (item.rect1.isVisible()) {
                members.remove(str);
                item.rect1.setVisible(false);
            } else {
                item.rect1.setVisible(true);
                members.add(str);
            }
            load();
        }
    }

    public void chooseCatg(FollowerComponentController item, LinkedList<String> str) {
        if (item.rect1.isVisible()) {
            item.rect1.setVisible(false);
            members.removeAll(str);
        } else {
            item.rect1.setVisible(true);
            members.addAll(str);
        }
    }

    public void makeTweet(String ID) throws IOException {
        if (clientManager.getConnected()) {
            overlay.setVisible(true);
            clientManager.sendTweets("SET_COMMENT_ID");
            clientManager.sendClicked(ID);
        }
    }

    public void tweetPage(String ID) throws IOException {
        if (clientManager.getConnected()) {
            clientManager.sendTweets("SET_TWEET_ID");
            clientManager.sendClicked(ID);
            new ChangeScene("../FXML/mainHub.fxml", grid);
        }
    }



    public void retweet(String ID) throws IOException {
        if (clientManager.getConnected()) {
            clientManager.sendTweets("RETWEET");
            clientManager.sendClicked(ID);
            grid.getChildren().clear();
            load();
        }
    }

    public void goToProfile(String owner, AnchorPane anchorPane) throws IOException {
        if (clientManager.getConnected()) {
            User user = Manager.getUser(owner);
            clientManager.sendUsers("SET_PROFILE");
            clientManager.sendClicked(gson.toJson(user));
            new ChangeScene("../FXML/mainHub.fxml", anchorPane);
        }
    }
}
