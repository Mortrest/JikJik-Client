//package client.Controllers;
//
//import client.Models.User;
//import client.network.ClientManager;
//import client.Manager;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.image.Image;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Circle;
//
//import java.io.File;
//import java.io.IOException;
//
//public class HomePage {
//    ClientManager clientManager;
//    User user;
//    @FXML
//    public GridPane grid;
//    @FXML
//    public Label nameLabel;
//    @FXML
//    public Circle proPic;
//    public Pane overlay1;
//    public GridPane overlayGrid;
//    @FXML
//    public Button sendMsg;
//    @FXML
//    private TextArea textArea, overlayText;
//    @FXML
//    private Pane overlay;
//
//    public void initialize() throws IOException {
//        user = Manager.getUser();
//        clientManager = Manager.getClientManager();
//        if (user.getProfilePic() != null) {
//            Image image = new Image(user.getProfilePic());
//            proPic.setFill(new ImagePattern(image));
//        }
//        nameLabel.setText("@" + user.getUsername() + " - " + user.getName());
//        Thread thread = new Thread(() -> {
//            while (true) {
//                try {
//                    // Send to server i want data bitch
//                    clientManager.sendClicked("DATA");
//                    String string = clientManager.read();
//
//                    // Then make pipe this data to the Load Data
//
//                    loadData();
//                    Thread.sleep(1000 );
//
//                } catch (IOException | InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        thread.start();
//
//    }
//
//    public void addTweet() throws IOException {
//        if (Tweets.getImage() == null) {
//            Tweets.makeTweet(textArea.getText(), "0", user.getUsername(), user.getFollowers());
//            grid.getChildren().clear();
//            loadData();
//            textArea.setText("");
//        } else {
//            File file = new File(Tweets.getImage());
//            Image image = new Image(file.toURI().toString());
//            Tweets.makeTweetImage(textArea.getText(), image.getUrl(), "0", user.getUsername(), user.getFollowers());
//            grid.getChildren().clear();
//            loadData();
//            textArea.setText("");
//        }
//    }
//
//    public void attach() throws IOException {
//        new ChangeProfilePicture(textArea,1);
//    }
//
//    public void loadData() throws IOException {
//        new TweetLoad(grid, textArea, 1, overlay,overlay1,overlayGrid,sendMsg,1).load();
//    }
//
//    public void closeOverlay() {
//        overlay.setVisible(false);
//        overlay1.setVisible(false);
//    }
//
//    public void sendComment() throws IOException {
//        if (overlayText.getText() != null) {
//            overlay.setVisible(false);
//            Tweets.makeTweet(overlayText.getText(), Tweets.getComment(), user.getUsername(), user.getFollowers());
//            loadData();
//        }
//    }
//
//
//}
//
//
