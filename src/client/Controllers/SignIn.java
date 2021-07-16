package client.Controllers;

import client.Config;
import client.Models.User;
import client.shared.SignInResponse;
import client.utils.ChangeScene;
import client.Manager;
import client.utils.Pages;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static client.Manager.clientManager;

public class SignIn {
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


    public void initialize() {
//        if (Users.getProfile() != Users.getCurrentUser() && Users.getProfile() != null) {
//            currentPage = profilePage;
//            profilePage.setVisible(true);
//        } else if (Tweets.getTweetID() != null) {
//            currentPage = eachTweet;
//            eachTweet.setVisible(true);
//        } else if (Chats.getRoomID() != null) {
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
                new ChangeScene("../FXML/rooms.fxml", signInBtn);
//                setHomePage();
            } else {
                incorrect.setVisible(true);
            }
        }
    }


    public void SignUp() throws IOException {
        new ChangeScene("../FXML/signUp.fxml", signInBtn);
    }

    public void setHomePage() {
        currentPage.setVisible(false);
        Pages.getPages().add(homePage);
        homePage.setVisible(true);
        currentPage = homePage;

    }

    public void setExplorePage() {
        currentPage.setVisible(false);
        Pages.getPages().add(explorePage);
        explorePage.setVisible(true);
        currentPage = explorePage;
    }

    public void setChatPage() {
        currentPage.setVisible(false);
        Pages.getPages().add(chatPage);
        chatPage.setVisible(true);
        currentPage = chatPage;
    }

    public void setNotifPage() {
        currentPage.setVisible(false);
        Pages.getPages().add(notifPage);
        notifPage.setVisible(true);
        currentPage = notifPage;
    }

    public void setSettingsPage() {
        currentPage.setVisible(false);
        Pages.getPages().add(settingsPage);
        settingsPage.setVisible(true);
        currentPage = settingsPage;
    }

    public void setProfilePage() {
        currentPage.setVisible(false);
        Pages.getPages().add(profilePage);
        profilePage.setVisible(true);
        currentPage = profilePage;
    }

}
