package client.Controllers;

import client.Config;
import client.utils.Manager;
import client.Models.User;
import client.network.ClientManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {
    User currentUser;
    ClientManager clientManager;
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
        currentUser = Manager.getUser();
        clientManager = Manager.getClientManager();
        if (currentUser.isLastSeenAvailable()){
            lastSeenText.setText("Currently Last Seen isn't Private");
        } else {
            lastSeenText.setText("Currently Last Seen is Private");
        }
        if (currentUser.isPrivate()){
            privateText.setText("Currently Contacts Only");
        } else {
            privateText.setText("Currently Everyone");
        }

    }

    public void logOut() throws IOException {
        backToReality();
    }
    public void delete() throws IOException {
        clientManager.sendUsers("DELETE");
//        Users.deleteProfile(currentUser);
        backToReality();
    }
    public void deactivate() throws IOException {
        clientManager.sendUsers("DEACTIVATE");
//        Users.deactivate(currentUser);
        backToReality();
    }
    public void changeLastSeen() throws IOException {
        if (currentUser.isLastSeenAvailable()){
            currentUser.setLastSeenAvailable(false);
            lastSeenText.setText("Currently Last Seen is Private");
        } else {
            currentUser.setLastSeenAvailable(true);
            lastSeenText.setText("Currently Last Seen isn't Private");
        }
        if (clientManager.getConnected()) {
            clientManager.sendUsers("CHANGE_LASTSEEN");
        } else {
            Manager.addOffline("CHANGE_LASTSEEN");
        }
//        Users.save();
    }
    public void changePrivate() throws IOException {
        if (currentUser.isPrivate()){
            currentUser.setPrivate(false);
            privateText.setText("Currently Everyone");
        } else {
            currentUser.setPrivate(true);
            privateText.setText("Currently Contacts Only");
        }
        if (clientManager.getConnected()) {
            clientManager.sendUsers("CHANGE_PRIVATE");
        } else {
            Manager.addOffline("CHANGE_PRIVATE");
        }
//        Users.save();
    }

    public void backToReality() throws IOException {
        clientManager.sendUsers("SAVE");
        clientManager.sendUsers("PROFILE_NULL");
        clientManager.sendChats("NULL");
        clientManager.sendUsers("PROFILE_NULL");
        clientManager.sendTweets("TWEET_NULL");
        clientManager.sendUsers("LOG_OUT");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "SignIn")));
        Stage window = (Stage) Salam.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void changePassword() throws IOException {
        if (currentUser.getPassword().equals(pass1.getText()) && pass2.getText() != null){
            if (clientManager.getConnected()) {
                clientManager.sendUsers("CHANGE_PASSWORD");
                clientManager.sendClicked(pass2.getText());
            } else {
                Manager.addOffline("CHANGE_PASSWORD");
                Manager.addOffline(pass2.getText());
            }
            pass1.setText("");
            pass2.setText("");
            wrong.setVisible(false);
        } else {
            wrong.setVisible(true);
            pass1.setText("");
        }
//        clientManager.sendUsers("SAVE");

//        Users.save();
    }
}
