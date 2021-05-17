package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Logic.Chats;
import sample.Models.Tweets;
import sample.Models.Users;
import sample.utils.Pages;

import java.io.IOException;

public class SettingsController {
    @FXML
    AnchorPane Salam;
    @FXML
    private PasswordField pass1;

    @FXML
    private PasswordField pass2;

    @FXML
    private Label lastSeenText,wrong;

    @FXML
    private Label privateText;


    public void initialize(){
        if (Users.getCurrentUser().isLastSeenAvailable()){
            lastSeenText.setText("Currently Last Seen isn't Private");
        } else {
            lastSeenText.setText("Currently Last Seen is Private");
        }
        if (Users.getCurrentUser().isPrivate()){
            privateText.setText("Currently Contacts Only");
        } else {
            privateText.setText("Currently Everyone");
        }

    }

    public void logOut() throws IOException {
        backToReality();
    }
    public void delete() throws IOException {
        Users.deleteProfile(Users.getCurrentUser());
        backToReality();
    }
    public void deactivate() throws IOException {
        Users.deactivate(Users.getCurrentUser());
        backToReality();
    }
    public void changeLastSeen(){
        if (Users.getCurrentUser().isLastSeenAvailable()){
            Users.getCurrentUser().setLastSeenAvailable(false);
            lastSeenText.setText("Currently Last Seen is Private");
        } else {
            Users.getCurrentUser().setLastSeenAvailable(true);
            lastSeenText.setText("Currently Last Seen isn't Private");
        }
        Users.save();
    }
    public void changePrivate(){
        if (Users.getCurrentUser().isPrivate()){
            Users.getCurrentUser().setPrivate(false);
            privateText.setText("Currently Everyone");
        } else {
            Users.getCurrentUser().setPrivate(true);
            privateText.setText("Currently Contacts Only");
        }
        Users.save();
    }


    public void backToReality() throws IOException {
        Users.save();
        Users.setCurrentUser(null);
        Chats.setRoomID(null);
        Users.setProfile(null);
        Tweets.setTweetID(null);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Pages.getPages().clear();
        Parent root = fxmlLoader.load(getClass().getResource("../FXML/SignIn.fxml"));
        Stage window = (Stage) Salam.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void changePassword(){
        if (Users.getCurrentUser().getPassword().equals(pass1.getText()) && pass2.getText() != null){
            Users.getCurrentUser().setPassword(pass2.getText());
            wrong.setVisible(false);
        } else {
            wrong.setVisible(true);
            pass1.setText("");
        }
        Users.save();
    }
}
