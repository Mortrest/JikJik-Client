package sample.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import sample.Models.Chats;
import sample.Models.Tweets;
import sample.Models.User;
import sample.Models.Users;
import sample.utils.ChangeScene;
import sample.utils.LoadComponent;
import sample.utils.TweetLoad;

import java.io.IOException;
import java.util.LinkedList;

public class ProfileController {
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
        User user = Users.getProfile();
        username.setText("@" + user.getUsername());
        if (user.getInfo() != null) {
            bio.setText(user.getInfo());
        } else {
            bio.setText("No bio");
        }
        message.setOnMouseClicked(e -> sendMsg());
        fNames.setText(user.getFirstName() + " " + user.getLastName());
        if (Users.getProfile().getProfilePic() != null) {
            Image image = new Image(Users.getProfile().getProfilePic());
            profilePic.setFill(new ImagePattern(image));
        }
        flwrsCount.setText(Integer.toString(user.getFollowers().size()));
        flwingCount.setText(Integer.toString(user.getFollowing().size()));
        if (user.getUsername().equals(Users.getCurrentUser().getUsername())) {
            followBtn.setVisible(false);
            isFollowing.setText("It's you bro");
        } else {
            followBtn.setVisible(true);
            blockBtn.setVisible(true);
            if (Users.getCurrentUser().getBlackList().contains(Users.getProfile().getUsername())) {
                blockBtn.setText("Unblock");
            }
            if (Users.getCurrentUser().getFollowers().contains(user.getUsername())) {
                isFollowing.setText(user.getUsername() + " is Following You!");
            } else {
                isFollowing.setText(user.getUsername() + " isn't Following You!");
            }
            if (Users.getCurrentUser().getFollowing().contains(user.getUsername())) {
                followBtn.setText("Unfollow");
            } else {
                followBtn.setText("Follow");
            }
        }
        loadData();
        grid.setLayoutY(90);
        grid.setLayoutX(-40);
    }

    private void sendMsg() {
        if (Chats.searchRoom(Users.getCurrentUser().getUsername(), Users.getProfile().getUsername()) == null) {
            if (Users.getProfile().isPrivate()) {
                if (Users.getProfile().getFollowers().contains(Users.getCurrentUser().getUsername())) {
                    Chats.makeRoom(Users.getCurrentUser().getUsername(), Users.getProfile().getUsername());
                }
            } else {
                Chats.makeRoom(Users.getCurrentUser().getUsername(), Users.getProfile().getUsername());
            }
        }
    }

    public void back() throws IOException {
        Users.setProfile(Users.getCurrentUser());
        new ChangeScene("../FXML/sample.fxml", grid);
    }

    public void loadData() throws IOException {
        new TweetLoad(grid, textArea, 4, overlay, 1).load();
    }

    public void closeOverlay() {
        overlay.setVisible(false);
        overlay1.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (overlayText.getText() != null) {
            overlay.setVisible(false);
            Tweets.makeTweet(overlayText.getText(), Tweets.getComment(), Users.getCurrentUser().getUsername(), Users.getCurrentUser().getFollowers());
            grid.getChildren().clear();
            loadData();
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
        overlay1.setVisible(true);
        overlayGrid.getChildren().clear();
        LinkedList<String> users;
        if (type == 1) {
            users = Users.getCurrentUser().getFollowers();
        } else if (type == 2) {
            users = Users.getCurrentUser().getFollowing();
        } else {
            users = Users.getCurrentUser().getBlackList();
        }
        for (String str : users) {
            LoadComponent loadComponent = new LoadComponent("../FXML/FollowersComponent.fxml");
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
        Users.setProfile(Users.searchUsername(str));
        new ChangeScene("../FXML/sample.fxml", grid);
    }

    public void blockProfile() {
        if (blockBtn.getText().equals("Block")) {
            blockBtn.setText("Unblock");
        } else {
            blockBtn.setText("Block");
        }
        Users.blockProfile(Users.getCurrentUser(), Users.getProfile().getUsername());
    }

    public void follow() throws IOException {
        Users.followProfile(Users.getCurrentUser(), Users.getProfile().getUsername());
        if (followBtn.getText().equals("Unfollow")) {
            followBtn.setText("Follow");
        } else {
            followBtn.setText("Unfollow");
        }
    }
}

