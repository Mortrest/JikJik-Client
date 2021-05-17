package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.Logic.Chats;
import sample.ModelLoader;
import sample.Models.*;

import java.io.IOException;
import java.util.List;

public class SignInController {
    List<AnchorPane> pages;
    public AnchorPane login;
    public AnchorPane home;
    public AnchorPane explore;
    public AnchorPane current;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private TextField signInTextField;
    @FXML
    private Label incorrect;
    @FXML
    private Button button,btnT1,btnT2;

    public void SignIn() throws IOException {
        ModelLoader m = new ModelLoader();
        Tweets tweets = new Tweets(m);
        Notifs notifs = new Notifs(m);
        Chats chats = new Chats(m);
        Users users = new Users(m,tweets,chats,notifs);
        Users.setCurrentUser(Users.searchUsername(signInTextField.getText()));
        home.setVisible(true);
        login.setVisible(false);

//        if (Users.signIn(signInTextField.getText(),signInPassword.getText())){
//            home.setVisible(true);
//            login.setVisible(false);
//            current = home;
//            Users.setCurrentUser(Users.searchUsername(signInTextField.getText()));
//            System.out.println(Users.getCurrentUser());
//        } else {
//            incorrect.setVisible(true);
//            signInPassword.setText("");
//
//        }

    }
    public void homePage(){
        current.setVisible(false);
        home.setVisible(true);
        current = home;
    }
    public void explorePage(){
        current.setVisible(false);
        explore.setVisible(true);
        current = explore;
    }

}
