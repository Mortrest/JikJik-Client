package client.Controllers;

import client.Config;
import client.Models.User;
import client.network.ClientManager;
import client.shared.MakeTweetResponse;
import client.utils.ChangeScene;
import client.utils.Manager;
import client.shared.TweetLoad;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ExploreController {
    Gson gson;
    ClientManager clientManager;
    User currentUser;
    @FXML
    public GridPane grid;
    @FXML
    public TextField searchArea;
    public TextArea textArea;
    @FXML
    public Label found;
    public Pane overlay1;
    public GridPane overlayGrid;
    public Button sendMsg;
    @FXML
    private TextArea overlayText;
    @FXML
    private Pane overlay;

    public void initialize() throws IOException {
        this.gson = new Gson();
        this.clientManager = Manager.getClientManager();
        this.currentUser = Manager.getUser();
        // Thread

        loadData();
        grid.setLayoutX(-40);
    }

    public void search() throws IOException {
        if (clientManager.getConnected()) {
            User target = Manager.getUser(searchArea.getText());
            if (target != null) {
                found.setVisible(false);
                // Profile Visits
                clientManager.sendUsers("SET_PROFILE");
                clientManager.sendClicked(gson.toJson(target));
                new ChangeScene(Config.getConfig("mainConfig").getProperty((String.class), "Profile"),grid);
            } else {
                found.setVisible(true);
                searchArea.setText("");
            }
        }
    }

    public void loadData() throws IOException {
        new TweetLoad(grid, textArea, 3, overlay,overlay1,overlayGrid,sendMsg,1,clientManager,currentUser).load();
    }

    public void closeOverlay(){
        overlay.setVisible(false);
        overlay1.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (clientManager.getConnected()) {
            if (overlayText.getText() != null && clientManager.getConnected()) {
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
}
