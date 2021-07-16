//package client.Controllers;
//
//import client.Manager;
//import client.Models.User;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.control.PasswordField;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class SettingsController {
//    User currentUser;
//    @FXML
//    AnchorPane Salam;
//    @FXML
//    private PasswordField pass1;
//
//    @FXML
//    private PasswordField pass2;
//
//    @FXML
//    private Label lastSeenText,wrong;
//
//    @FXML
//    private Label privateText;
//
//
//    public void initialize(){
//        currentUser = Manager.getUser();
//
//        if (currentUser.isLastSeenAvailable()){
//            lastSeenText.setText("Currently Last Seen isn't Private");
//        } else {
//            lastSeenText.setText("Currently Last Seen is Private");
//        }
//        if (currentUser.isPrivate()){
//            privateText.setText("Currently Contacts Only");
//        } else {
//            privateText.setText("Currently Everyone");
//        }
//
//    }
//
//    public void logOut() throws IOException {
//        backToReality();
//    }
//    public void delete() throws IOException {
//        Users.deleteProfile(currentUser);
//        backToReality();
//    }
//    public void deactivate() throws IOException {
//        Users.deactivate(currentUser);
//        backToReality();
//    }
//    public void changeLastSeen(){
//        if (currentUser.isLastSeenAvailable()){
//            currentUser.setLastSeenAvailable(false);
//            lastSeenText.setText("Currently Last Seen is Private");
//        } else {
//            currentUser.setLastSeenAvailable(true);
//            lastSeenText.setText("Currently Last Seen isn't Private");
//        }
//        Users.save();
//    }
//    public void changePrivate(){
//        if (currentUser.isPrivate()){
//            currentUser.setPrivate(false);
//            privateText.setText("Currently Everyone");
//        } else {
//            currentUser.setPrivate(true);
//            privateText.setText("Currently Contacts Only");
//        }
//        Users.save();
//    }
//
//
//    public void backToReality() throws IOException {
//        Users.save();
//        Users.setCurrentUser(null);
//        Chats.setRoomID(null);
//        Users.setProfile(null);
//        Tweets.setTweetID(null);
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        Pages.getPages().clear();
//        Parent root = fxmlLoader.load(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "SignIn")));
//        Stage window = (Stage) Salam.getScene().getWindow();
//        window.setScene(new Scene(root));
//    }
//
//    public void changePassword(){
//        if (currentUser.getPassword().equals(pass1.getText()) && pass2.getText() != null){
//            currentUser.setPassword(pass2.getText());
//            wrong.setVisible(false);
//        } else {
//            wrong.setVisible(true);
//            pass1.setText("");
//        }
//        Users.save();
//    }
//}
