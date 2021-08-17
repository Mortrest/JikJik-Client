package client.Controllers;

import client.Config;
import client.utils.Manager;
import client.Models.User;
import client.network.ClientManager;
import client.shared.SignInResponse;
import client.utils.ChangeScene;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignIn {
    Gson gson;
    ClientManager clientManager;
    User currentUser;
    User profile;
    public AnchorPane settingsPage;
    @FXML
    private AnchorPane homePage, explorePage, chatPage, currentPage, notifPage, eachChat, eachTweet, profilePage;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private TextField signInTextField;
    @FXML
    private Label incorrect;
    @FXML
    Button signInBtn, btnT1;


    public void initialize() throws IOException {
        gson = new Gson();
        clientManager = Manager.getClientManager();
        currentUser = Manager.getUser();

        // Check wether current user == null or not

//        clientManager.sendUsers("GET_PROFILE");
//        String str = clientManager.read();
//        if (str.equals("NULL")){
//            this.profile = null;
//        } else {
//            this.profile = gson.fromJson(str,User.class);
//        }
//        clientManager.sendTweets("GET_TWEET_ID");
//        str = clientManager.read();
//        String tweetID = null;
//        if (!str.equals("NULL")){
//            tweetID = str;
//        }
//        String roomID = null;
//        clientManager.sendChats("GET_ROOMID");
//        str = clientManager.read();
//        if (!str.equals("NULL")){
//            roomID = str;
//        }
//        if (profile != currentUser && profile != null) {
//            currentPage = profilePage;
//            profilePage.setVisible(true);
//        } else if (tweetID != null) {
//            currentPage = eachTweet;
//            eachTweet.setVisible(true);
//        } else if (roomID != null) {
//            currentPage = chatPage;
//            chatPage.setVisible(true);
//        } else {
//            if (Pages.getPages().size() >= 2) {
//                if (Pages.getPages().getLast() == null) {
//                    currentPage = homePage;
//                    setHomePage();
//                } else {
//                    currentPage = Pages.getPages().getLast();
//                    if (currentPage.getId().equals("explorePage")) {
//                        setExplorePage();
//                    } else if (currentPage.getId().equals("profilePage")) {
//                        setProfilePage();
//                    } else if (currentPage.getId().equals("chatPage")) {
//                        setChatPage();
//                    } else {
//                        setHomePage();
//                    }
//
//                }
//            } else {
//                Pages.getPages().addLast(homePage);
//                currentPage = homePage;
//            }
//        }
//        Pages.getPages().addLast(homePage);
//        currentPage = homePage;
    }

    public void refresh() throws IOException {
        new ChangeScene(Config.getConfig("mainConfig").getProperty((String.class), "Sample"), signInBtn);
    }

    public void SignIn() throws IOException {
        if (!signInTextField.getText().equals("") && !signInPassword.getText().equals("")) {
            clientManager.sendClicked("SIGN_IN");
            Gson gson = new Gson();
            SignInResponse sr = new SignInResponse(signInTextField.getText(),signInPassword.getText());
            clientManager.sendClicked(gson.toJson(sr));
            String res = clientManager.read();
            if (res.equals("Signed In")) {
                Manager.setAuthToken(clientManager.read());
                String us = clientManager.read();
                User user = gson.fromJson(us, User.class);
                Manager.setUser(user);
                new ChangeScene("../FXML/mainHub.fxml", signInBtn);
//                setHomePage();
            } else {
                incorrect.setVisible(true);
            }
        }
    }


    public void SignUp() throws IOException {
        new ChangeScene("../FXML/signUp.fxml", signInBtn);
    }
//
//    public void setHomePage() {
//        currentPage.setVisible(false);
//        Pages.getPages().add(homePage);
//        homePage.setVisible(true);
//        currentPage = homePage;
//
//    }
//
//    public void setExplorePage() {
//        currentPage.setVisible(false);
//        Pages.getPages().add(explorePage);
//        explorePage.setVisible(true);
//        currentPage = explorePage;
//    }
//
//    public void setChatPage() {
//        currentPage.setVisible(false);
//        Pages.getPages().add(chatPage);
//        chatPage.setVisible(true);
//        currentPage = chatPage;
//    }
//
//    public void setNotifPage() {
//        currentPage.setVisible(false);
//        Pages.getPages().add(notifPage);
//        notifPage.setVisible(true);
//        currentPage = notifPage;
//    }
//
//    public void setSettingsPage() {
//        currentPage.setVisible(false);
//        Pages.getPages().add(settingsPage);
//        settingsPage.setVisible(true);
//        currentPage = settingsPage;
//    }
//
//    public void setProfilePage() {
//        currentPage.setVisible(false);
//        Pages.getPages().add(profilePage);
//        profilePage.setVisible(true);
//        currentPage = profilePage;
//    }

}
