package client.Controllers;

import client.Models.User;
import client.network.ClientManager;
import client.shared.MakeTweetResponse;
import client.shared.TweetLoad;
import client.utils.ChangeProfilePicture;
import client.utils.Manager;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import java.io.IOException;

public class HomePage {
    ClientManager clientManager;
    User currentUser;
    Gson gson;
    @FXML
    public GridPane grid;
    @FXML
    public Label nameLabel;
    @FXML
    public Circle proPic;
    public Pane overlay1;
    public GridPane overlayGrid;
    @FXML
    public Button sendMsg;
    @FXML
    private TextArea textArea, overlayText;
    @FXML
    private Pane overlay;

    public void initialize() throws IOException {
        gson = new Gson();
        currentUser = Manager.getUser();
        clientManager = Manager.getClientManager();
        if (currentUser.getProfilePic() != null) {
            Image image = new Image(currentUser.getProfilePic());
            proPic.setFill(new ImagePattern(image));
        }
        nameLabel.setText("@" + currentUser.getUsername() + " - " + currentUser.getName());
        Thread thread = new Thread(() -> {
            while (Manager.pages.getLast().equals("home")) {
                try {
                    if (!Manager.pages.getLast().equals("home")) {
                        break;
                    }
                    System.out.println("moooo");
                    Platform.runLater(() -> {
                        try {
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
    }

    public void addTweet() throws IOException {
        if (clientManager.getConnected()) {
            clientManager.sendTweets("GET_IMAGE");
            String str = clientManager.read();
            if (str == null) {
                MakeTweetResponse mk = new MakeTweetResponse(textArea.getText(), "0", currentUser.getUsername(), currentUser.getFollowers(), null, false);
                clientManager.sendTweets("MAKE_TWEET");
                clientManager.sendClicked(gson.toJson(mk));
//            Tweets.makeTweet(textArea.getText(), "0", currentUser.getUsername(), currentUser.getFollowers());
                grid.getChildren().clear();
                loadData();
                textArea.setText("");
            } else {
//            File file = new File(Tweets.getImage());
//            File file = new File(str);
//            Image image = new Image(file.toURI().toString());
                MakeTweetResponse mk = new MakeTweetResponse(textArea.getText(), "0", currentUser.getUsername(), currentUser.getFollowers(), str, true);
                clientManager.sendTweets("MAKE_TWEET");
                clientManager.sendClicked(gson.toJson(mk));
//            Tweets.makeTweetImage(textArea.getText(), image.getUrl(), "0", currentUser.getUsername(), currentUser.getFollowers());
                grid.getChildren().clear();
                loadData();
                textArea.setText("");
            }
        }
    }

    public void attach() throws IOException {
        if (clientManager.getConnected()) {
            new ChangeProfilePicture(textArea, 1);
        }
    }

    public void loadData() throws IOException {
        new TweetLoad(grid, textArea, 1, overlay,overlay1,overlayGrid,sendMsg,1,clientManager,currentUser).load();
    }

    public void closeOverlay() {
        overlay.setVisible(false);
        overlay1.setVisible(false);
    }

//    public void sendComment() throws IOException {
//        if (overlayText.getText() != null) {
//            overlay.setVisible(false);
//            Tweets.makeTweet(overlayText.getText(), Tweets.getComment(), user.getUsername(), user.getFollowers());
//            loadData();
//        }
//    }
public void sendComment() throws IOException {
        if (clientManager.getConnected()) {
            if (overlayText.getText() != null) {
                overlay.setVisible(false);
                clientManager.sendTweets("COMMENT_ID");
                String ID = clientManager.read();
                MakeTweetResponse mk = new MakeTweetResponse(overlayText.getText(), ID, currentUser.getUsername(), currentUser.getFollowers(), null, false);
                clientManager.sendTweets("MAKE_TWEET");
                Gson gson = new Gson();
                clientManager.sendClicked(gson.toJson(mk));
//            Tweets.makeTweet(overlayText.getText(),Tweets.getComment(),Users.getCurrentUser().getUsername(),Users.getCurrentUser().getFollowers());
                grid.getChildren().clear();
                loadData();
            }
        }
}

}


