package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import sample.Models.Tweets;
import sample.Models.Users;
import sample.utils.ChangeScene;
import sample.utils.TweetLoad;

import java.io.IOException;

public class ExploreController {
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
        loadData();
        grid.setLayoutX(-40);
    }

    public void search() throws IOException {
        if (Users.searchUsername(searchArea.getText()) != null){
            found.setVisible(false);
            Users.setProfile(Users.searchUsername(searchArea.getText()));
            new ChangeScene("../FXML/sample.fxml",grid);
        } else {
            found.setVisible(true);
            searchArea.setText("");
        }
    }


    public void loadData() throws IOException {
        new TweetLoad(grid, textArea, 3, overlay,overlay1,overlayGrid,sendMsg,1).load();
    }

    public void closeOverlay(){
        overlay.setVisible(false);
        overlay1.setVisible(false);
    }

    public void sendComment() throws IOException {
        if (overlayText.getText() != null){
            overlay.setVisible(false);
            Tweets.makeTweet(overlayText.getText(),Tweets.getComment(),Users.getCurrentUser().getUsername(),Users.getCurrentUser().getFollowers());
            grid.getChildren().clear();
            loadData();
        }
    }

}
