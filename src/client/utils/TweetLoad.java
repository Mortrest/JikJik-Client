package client.utils;

import client.Config;
import client.Controllers.FollowerComponentController;
import client.Controllers.TweetComponentController;
import client.Controllers.TweetComponentImageController;
import client.Manager;
import client.Models.Room;
import client.Models.Tweet;
import client.Models.User;
import client.network.ClientManager;
import client.shared.GetTweetsResponse;
import client.shared.MakeChatResponse;
import client.shared.RoomResponse;
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

    public TweetLoad(GridPane grid, TextArea textArea, int type, Pane overlay, int isComment,ClientManager clientManager,User currentUser) {
        this.clientManager = clientManager;
        this.currentUser = currentUser;
        this.grid = grid;
        this.gson = new Gson();
        this.type = type;
        this.textArea = textArea;
        this.overlay = overlay;
        this.isComment = isComment;
    }

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
        LinkedList<Tweet> tw;
        if (isComment == 1) {
            if (type == 4) {
                clientManager.sendUsers("SHOW_TWEET");
                clientManager.sendClicked("2");
                GetTweetsResponse gs = gson.fromJson(clientManager.read(), GetTweetsResponse.class);
                tw = gs.getTweets();
//                tw = Users.getTweets().showTweetOwnPage(Users.class,Users.getProfile().getUsername(), 2);
            } else {
                clientManager.sendUsers("SHOW_TWEET");
                clientManager.sendClicked(String.valueOf(type));
                GetTweetsResponse gs = gson.fromJson(clientManager.read(), GetTweetsResponse.class);
                tw = gs.getTweets();
            }
        } else {
            clientManager.sendTweets("GET_TWEET_ID");
            String ID = clientManager.read();
            clientManager.sendUsers("GET_COMMENTS");
            clientManager.sendClicked(ID);
            GetTweetsResponse gs = gson.fromJson(clientManager.read(), GetTweetsResponse.class);
//             Users.getTweets().getComments(ID);
            tw = gs.getTweets();
        }

        for (int i = 1; i < tw.size() + 1; i++) {
            if (tw.get(i - 1).getReported() <= 5) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                if (tw.get(i - 1).getImage() == null) {
                    fxmlLoader.setLocation(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "TweetComponent")));
                    AnchorPane anchorPane = fxmlLoader.load();
                    TweetComponentController itemController = fxmlLoader.getController();
                    User user = Manager.getUser(tw.get(i - 1).getOwner());
                    if (user.getProfilePic() != null) {
                        itemController.getProfilePic().setFill(new ImagePattern(new Image(user.getProfilePic())));
                    }
                    int finalI = i - 1;
                    itemController.setTweetID(tw.get(i - 1).getID());
                    itemController.loadData();
                    int finalI1 = i;
                    itemController.getGoToProfile().setOnMouseClicked(e -> {
                        try {
                            goToProfile(tw.get(finalI1 - 1).getOwner(), anchorPane);
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
                    itemController.getComment().setOnMouseClicked(e -> {
                        try {
                            makeTweet(tw.get(finalI).getID());
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    itemController.getForward().setOnMouseClicked(e -> {
                        try {
                            clientManager.sendTweets("SET_FORWARD_ID");
                            clientManager.sendClicked(tw.get(finalI).getID());
//                            Tweets.setForwardTweetID(tw.get(finalI).getID());
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
                    itemController.setNameLabel("@" + tw.get(i - 1).getOwner() + " - " + user.getName());
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
                    User user = Manager.getUser(tw.get(i - 1).getOwner());
                    itemController.getProfilePic().setFill(new ImagePattern(new Image(user.getProfilePic())));
                    int finalI = i - 1;
                    itemController.setTweetID(tw.get(i - 1).getID());
                    itemController.loadData();
                    itemController.getLikeCount().setText(Integer.toString(tw.get(i - 1).getLikes().size()));
                    itemController.getForward().setOnMouseClicked(e -> {
                        try {
                            clientManager.sendTweets("SET_FORWARD_ID");
                            clientManager.sendClicked(tw.get(finalI).getID());
//                            Tweets.setForwardTweetID(tw.get(finalI).getID());
                            loadFlw();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                    int finalI2 = i;
                    itemController.getGoToProfile().setOnMouseClicked(e -> {
                        try {
                            goToProfile(tw.get(finalI2 - 1).getOwner(), anchorPane);
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
                    itemController.getComment().setOnMouseClicked(e -> {
                        try {
                            makeTweet(tw.get(finalI).getID());
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
                    itemController.setNameLabel("@" + tw.get(i - 1).getOwner() + " - " + user.getName());
                    itemController.setTweetLabel(tw.get(i - 1).getText());
                    grid.add(anchorPane, 1, grid.getRowCount() + 1);
                    GridPane.setMargin(anchorPane, new Insets(10));
                }
            }
        }
    }

    // Send Messages

    public void sendMsg(Tweet tweet) throws IOException {
        for (String user : members) {
//            Room room1 = Chats.searchRoomID(Chats.searchGroup(user));
            clientManager.sendChats("SEARCH_GROUP");
            clientManager.sendClicked(user);
            String roomID = clientManager.read();
            clientManager.sendChats("SEARCH_ROOM");
            clientManager.sendClicked(roomID);
            Room room1 = gson.fromJson(clientManager.read(),Room.class);
            System.out.println("1");
            if (room1 != null) {
                MakeChatResponse mk = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room1.getRoomID(), null, false,false);
                clientManager.sendChats("MAKE_CHAT");
                clientManager.sendClicked(gson.toJson(mk));

//                Chats.makeChat(tweet.getText(), currentUser.getUsername(), room1.getRoomID());
                if (tweet.getImage() != null) {
                    MakeChatResponse mks = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room1.getRoomID(), tweet.getImage(), true,false);
                    clientManager.sendChats("MAKE_CHAT");
                    clientManager.sendClicked(gson.toJson(mks));

//                    Chats.makeImageChat(tweet.getText(), currentUser.getUsername(), room1.getRoomID(), tweet.getImage());
                }
            } else {
                System.out.println("2");
                clientManager.sendChats("SEARCH_ROOM2");
                clientManager.sendClicked(user);
                String str = clientManager.read();
                if (str.equals("NULL")){
//                    Chats.makeRoom(currentUser.getUsername(), user);
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
                MakeChatResponse mk = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room.getRoomID(), null, false,false);
//                Chats.makeChat(tweet.getText(), currentUser.getUsername(), Chats.searchRoom(currentUser.getUsername(), user).getRoomID());
                clientManager.sendChats("MAKE_CHAT");
                clientManager.sendClicked(gson.toJson(mk));
                if (tweet.getImage() != null) {
                    MakeChatResponse mks = new MakeChatResponse(tweet.getText(), currentUser.getUsername(), room.getRoomID(), tweet.getImage(), true,false);
//                Chats.makeChat(tweet.getText(), currentUser.getUsername(), Chats.searchRoom(currentUser.getUsername(), user).getRoomID());
                    clientManager.sendChats("MAKE_CHAT");
                    clientManager.sendClicked(gson.toJson(mks));
//                    Chats.makeImageChat(tweet.getText(), currentUser.getUsername(), user, tweet.getImage());
                }
            }
        }
        overlay1.setVisible(false);
    }

    public void loadFlw() throws IOException {
        overlay1.setVisible(true);
        overlayGrid.getChildren().clear();
        clientManager.sendUsers("GET_PROFILE");
        User us = gson.fromJson(clientManager.read(), User.class);
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

    LinkedList<String> members = new LinkedList<>();

    public void chooseFlwr(FollowerComponentController item, String str) throws IOException {
        if (item.rect1.isVisible()) {
            members.remove(str);
            item.rect1.setVisible(false);
        } else {
            item.rect1.setVisible(true);
            members.add(str);
        }
        load();
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
        overlay.setVisible(true);
        clientManager.sendTweets("SET_COMMENT_ID");
        clientManager.sendClicked(ID);

//        Tweets.setComment(ID);
    }

    public void tweetPage(String ID) throws IOException {
        clientManager.sendTweets("SET_TWEET_ID");
        clientManager.sendClicked(ID);
//        Tweets.setTweetID(ID);
        new ChangeScene("../FXML/mainHub.fxml", grid);
    }



    public void retweet(String ID) throws IOException {
        clientManager.sendTweets("RETWEET");
        clientManager.sendClicked(ID);
//        Tweets.reTweet(Tweets.search(ID), currentUser);
        grid.getChildren().clear();
        load();
    }

    public void goToProfile(String owner, AnchorPane anchorPane) throws IOException {
        User user = Manager.getUser(owner);
        clientManager.sendUsers("SET_PROFILE");
        clientManager.sendClicked(gson.toJson(user));
//        Users.setProfile(user);
        new ChangeScene("../FXML/mainHub.fxml", anchorPane);

    }
}
