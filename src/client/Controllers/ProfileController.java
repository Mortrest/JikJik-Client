package client.Controllers;

import client.Config;
import client.utils.Manager;
import client.Models.User;
import client.network.ClientManager;
import client.shared.MakeTweetResponse;
import client.utils.ChangeScene;
import client.utils.LoadComponent;
import client.shared.TweetLoad;
import com.google.gson.Gson;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.LinkedList;

public class ProfileController {
    User currentUser;
    ClientManager clientManager;
    Gson gson;
    User profile;
    @FXML
    public Label followersLabel, followingsLabel;
    public GridPane overlayGrid;
    @FXML
    public Circle profilePic;
    @FXML
    public AnchorPane editPage;
    @FXML
    public AnchorPane Salam;
    @FXML
    public AnchorPane profilePage;
    public Button blockBtn;
    @FXML
    public FontAwesomeIconView message;
    public FontAwesomeIconView lock;
    public Label privateAcc;
    public Pane overlay2;
    public GridPane overlayGrid1;
    public Button sendMsg2;
    @FXML
    private Label fNames;

    @FXML
    private Label username;

    @FXML
    private Label bio;

    @FXML
    private Label flwrsCount;

    @FXML
    private Label flwingCount;

    @FXML
    private Button followBtn;

    @FXML
    private Label isFollowing;

    @FXML
    private GridPane grid;
    @FXML
    private TextArea textArea, overlayText;

    @FXML
    private Pane overlay, overlay1;


    public void initialize() throws IOException {
        this.clientManager = Manager.getClientManager();
        this.currentUser = Manager.getUser();
        this.gson = new Gson();
        if (clientManager.getConnected()) {
            clientManager.sendUsers("GET_PROFILE");
            String a = clientManager.read();
            if (a.equals("NULL")) {
                this.profile = currentUser;
            } else {
                this.profile = gson.fromJson(a, User.class);
            }
        } else {
            this.profile = currentUser;
        }
//        User user = Users.getProfile();
        username.setText("@" + profile.getUsername());
        if (profile.getInfo() != null) {
            bio.setText(profile.getInfo());
        } else {
            bio.setText("No bio");
        }
        message.setOnMouseClicked(e -> {
            try {
                sendMsg();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        fNames.setText(profile.getFirstName() + " " + profile.getLastName());
        if (profile.getProfilePic() != null) {
            Image image = new Image(profile.getProfilePic());
            profilePic.setFill(new ImagePattern(image));
        }
        flwrsCount.setText(Integer.toString(profile.getFollowers().size()));
        flwingCount.setText(Integer.toString(profile.getFollowing().size()));
        if (profile.getUsername().equals(currentUser.getUsername())) {
            followBtn.setVisible(false);
            isFollowing.setText("It's you bro");
        } else {
            followBtn.setVisible(true);
            blockBtn.setVisible(true);
            if (currentUser.getBlackList().contains(profile.getUsername())) {
                blockBtn.setText("Unblock");
            }
            if (currentUser.getFollowers().contains(profile.getUsername())) {
                isFollowing.setText(profile.getUsername() + " is Following You!");
            } else {
                isFollowing.setText(profile.getUsername() + " isn't Following You!");
            }
            if (currentUser.getFollowing().contains(profile.getUsername())) {
                followBtn.setText("Unfollow");
            } else {
                followBtn.setText("Follow");
            }
        }

        Thread thread = new Thread(() -> {
            while (Manager.pages.getLast().equals("profile")) {
                try {
                    if (!Manager.pages.getLast().equals("profile")) {
                        break;
                    }
                    Platform.runLater(() -> {
                        try {
                            System.out.println("hello profile");
                            loadData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(1590 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


//        loadData();
        grid.setLayoutY(90);
        grid.setLayoutX(-40);
    }

    private void sendMsg() throws IOException {
        if (clientManager.getConnected()) {
            clientManager.sendChats("SEARCH_ROOM2");
            clientManager.sendClicked(profile.getUsername());
            String str = clientManager.read();
            if (str.equals("NULL")) {
                if (profile.isPrivate()) {
                    if (profile.getFollowers().contains(currentUser.getUsername())) {
                        clientManager.sendChats("MAKE_ROOM");
                        clientManager.sendClicked(profile.getUsername());
//                    Chats.makeRoom(currentUser.getUsername(), profile.getUsername());
                    }
                } else {
                    clientManager.sendChats("MAKE_ROOM");
                    clientManager.sendClicked(profile.getUsername());

//                Chats.makeRoom(currentUser.getUsername(), profile.getUsername());
                }
            }
        }
    }

    public void back() throws IOException {
        if (clientManager.getConnected()) {
            clientManager.sendUsers("SET_PROFILE");
            clientManager.sendClicked(gson.toJson(currentUser));
        }
//        Users.setProfile(currentUser);
        new ChangeScene(Config.getConfig("mainConfig").getProperty((String.class), "MainHub"), grid);
    }

    public void loadData() throws IOException {
//        clientManager.sendUsers("GET_PROFILE");
//        User profile = gson.fromJson(clientManager.read(),User.class);
        if (currentUser.getFollowing().contains(profile.getUsername()) || !profile.isPrivate() || profile.getUsername().equals(currentUser.getUsername())) {
            privateAcc.setVisible(false);
            lock.setVisible(false);
//            new TweetLoad(grid, textArea, 4, overlay, 1).load();
            System.out.println(currentUser.getUsername());
            new TweetLoad(grid, textArea, 4, overlay,overlay2,overlayGrid1,sendMsg2,1,clientManager,currentUser).load();

        }
    }

    public void closeOverlay() {
        overlay.setVisible(false);
        overlay1.setVisible(false);
        overlay2.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (clientManager.getConnected()){
        if (overlayText.getText() != null) {
            overlay.setVisible(false);
            clientManager.sendTweets("GET_COMMENT");
            String str = clientManager.read();
            MakeTweetResponse mk = new MakeTweetResponse(overlayText.getText(), str, currentUser.getUsername(), currentUser.getFollowers(), null, false);
            clientManager.sendTweets("MAKE_TWEET");
            clientManager.sendClicked(gson.toJson(mk));
//            Tweets.makeTweet(overlayText.getText(), str, currentUser.getUsername(), currentUser.getFollowers());
            grid.getChildren().clear();
            loadData();
        }
        }
    }

    public void editProfile() {
        editPage.setVisible(true);
//        Salam.setVisible(false);
    }

    public void followersOverlay() throws IOException {
        loadFlw(1);
    }

    public void followingsOverlay() throws IOException {
        loadFlw(2);
    }

    public void blackOverlay() throws IOException {
        loadFlw(3);
    }

    // 1 Followers 2 Followings
    public void loadFlw(int type) throws IOException {
//        if (clientManager.getConnected())
        overlay1.setVisible(true);
        overlayGrid.getChildren().clear();
        LinkedList<String> users;
//        clientManager.sendUsers("GET_PROFILE");
//        User profile = gson.fromJson(clientManager.read(),User.class);
        if (type == 1) {
            users = profile.getFollowers();
        } else if (type == 2) {
            users = profile.getFollowing();
        } else {
            users = profile.getBlackList();
        }
        for (String str : users) {
            LoadComponent loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "FollowersComponent"));
            AnchorPane anchorPane = loadComponent.loadAnchor();
            FollowerComponentController item = loadComponent.loadFxml().getController();
            item.setName("@" + str);
            item.getPane().setOnMouseClicked(e -> {
                try {
                    goToPage(str);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });
            overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
            overlayGrid.setLayoutX(-150);
            overlayGrid.setLayoutY(-25);
            GridPane.setMargin(anchorPane, new Insets(-25));
        }
    }

    public void goToPage(String str) throws IOException {
        if (clientManager.getConnected()) {
            User us = Manager.getUser(str);
            clientManager.sendUsers("SET_PROFILE");
            clientManager.sendClicked(gson.toJson(us));
//        Users.setProfile(Users.searchUsername(str));
            new ChangeScene("../FXML/mainHub.fxml", grid);
        }
    }

    public void blockProfile() throws IOException {
        if (clientManager.getConnected()) {
            if (blockBtn.getText().equals("Block")) {
                blockBtn.setText("Unblock");
            } else {
                blockBtn.setText("Block");
            }
            clientManager.sendUsers("GET_PROFILE");
            User profile = gson.fromJson(clientManager.read(), User.class);
            clientManager.sendUsers("BLOCK");
            clientManager.sendClicked(profile.getUsername());
//        Users.blockProfile(currentUser, profile.getUsername());
        }
    }

    public void follow() throws IOException {
        if (clientManager.getConnected()) {
            clientManager.sendUsers("GET_PROFILE");
            User profile = gson.fromJson(clientManager.read(), User.class);
            clientManager.sendUsers("FOLLOW");
            System.out.println(profile);
            clientManager.sendClicked(profile.getUsername());
//        Users.followProfile(currentUser, profile.getUsername());
            if (followBtn.getText().equals("Unfollow")) {
                followBtn.setText("Follow");
            } else {
                followBtn.setText("Unfollow");
            }
        }
    }
}

