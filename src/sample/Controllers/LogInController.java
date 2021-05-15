package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.Models.Chats;
import sample.Models.Tweets;
import sample.Models.User;
import sample.Models.Users;
import sample.utils.ChangeScene;
import sample.utils.Pages;

import java.io.IOException;

public class LogInController {
    public AnchorPane settingsPage;
    @FXML
    private AnchorPane homePage, explorePage, chatPage, currentPage, notifPage, eachChat,eachTweet,profilePage;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private TextField signInTextField;
    @FXML
    private Label incorrect;
    @FXML
     Button signInBtn, btnT1;


    public void initialize() {
        System.out.println(Pages.getPages());
        System.out.println(Users.getProfile());
        if (Users.getProfile() != Users.getCurrentUser() && Users.getProfile() != null){
            currentPage = profilePage;
            profilePage.setVisible(true);
        }
        else if (Tweets.getTweetID() != null){
            currentPage = eachTweet;
            eachTweet.setVisible(true);
        }
        else if (Chats.getRoomID() != null){
            currentPage = chatPage;
            chatPage.setVisible(true);
            System.out.println("no");
        } else {
            if (Pages.getPages().size() >= 2){
                if (Pages.getPages().getLast() == null){
                    currentPage = homePage;
                    setHomePage();
                } else {
                    currentPage = Pages.getPages().getLast();
                    if (currentPage.getId().equals("explorePage")){
                        setExplorePage();
                    }
                    else if (currentPage.getId().equals("profilePage")){
                        setProfilePage();
                    }
                    else if (currentPage.getId().equals("chatPage")){
                        setChatPage();
                    }
                    else {
                        setHomePage();
                    }

                }
            } else {
                Pages.getPages().addLast(homePage);
                currentPage = homePage;
            }
        }
    }

    public void refresh() throws IOException {
        new ChangeScene("../FXML/sample.fxml",signInBtn);
    }

    public void SignIn() throws IOException {
        if (Users.searchUsername(signInTextField.getText()) != null){
            User user = Users.searchUsername(signInTextField.getText());
            if (signInPassword.getText().equals(user.getPassword())){
                Users.setCurrentUser(user);
                Users.setProfile(Users.getCurrentUser());
                Pages.getPages().add(homePage);
                Parent root = FXMLLoader.load(getClass().getResource("../FXML/sample.fxml"));
                Stage window = (Stage) signInBtn.getScene().getWindow();
                window.setScene(new Scene(root));
            }
        }
        else {
            incorrect.setVisible(true);
            signInPassword.setText("");
        }
    }


    public void SignUp() throws IOException {
        new ChangeScene("../FXML/signUp.fxml",signInBtn);
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

    public void setSettingsPage(){
        currentPage.setVisible(false);
        Pages.getPages().add(settingsPage);
        settingsPage.setVisible(true);
        currentPage = settingsPage;
    }

    public void setProfilePage(){
        currentPage.setVisible(false);
        Pages.getPages().add(profilePage);
        profilePage.setVisible(true);
        currentPage = profilePage;
    }

//    public void setEachChat(){
//        currentPage.setVisible(false);
//        eachChat.setVisible(true);
//        currentPage = eachChat;
//    }
}
