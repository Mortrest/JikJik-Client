package client.Controllers;

import client.utils.Manager;
import client.Models.User;
import client.network.ClientManager;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import client.Config;
import client.utils.ChangeProfilePicture;

import java.io.IOException;

public class EditProfileController {
    ClientManager clientManager;
    User currentUser;
    Gson gson;
    @FXML
    private AnchorPane Salam;

    @FXML
    private Label wrong;

    @FXML
    private TextField bio;

    @FXML
    private TextField phoneNumber;

    @FXML
    private Circle proPic;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    public void initialize(){
        this.clientManager = Manager.getClientManager();
        this.currentUser = Manager.getUser();
        this.gson = new Gson();
        loadData();
    }

    public void loadData(){
        bio.setPromptText(currentUser.getInfo() == null ? "No Bio!" : currentUser.getInfo());
        email.setPromptText(currentUser.getEmail() == null ? "No Email!" : currentUser.getEmail());
        firstName.setPromptText(currentUser.getFirstName() == null ? "No First Name!" : currentUser.getFirstName());
        lastName.setPromptText(currentUser.getLastName() == null ? "No Last Name!" : currentUser.getLastName());
        phoneNumber.setPromptText(currentUser.getPhoneNumber() == null ? "No Phone number!" : currentUser.getPhoneNumber());
        if (currentUser.getProfilePic() != null){
            Image image = new Image(currentUser.getProfilePic());
            proPic.setFill(new ImagePattern(image));
        }
    }

    public void changeBio() {
        currentUser.setInfo(bio.getText());
        bio.setPromptText(bio.getText());
    }

    public void changeEmail() {
        currentUser.setEmail(email.getText());
        email.setPromptText(email.getText());
    }

    public void changeFName() {
        currentUser.setFirstName(firstName.getText());
        firstName.setPromptText(firstName.getText());
    }


    public void changeLName() {
        currentUser.setLastName(lastName.getText());
        lastName.setPromptText(lastName.getText());
    }


    public void changePhoneNumber() {
        currentUser.setPhoneNumber(phoneNumber.getText());
        phoneNumber.setPromptText(phoneNumber.getText());
    }


    public void changeProfile() throws IOException {
       new ChangeProfilePicture(bio);
       Image image = new Image(currentUser.getProfilePic());
       proPic.setFill(new ImagePattern(image));
    }

    public void categories() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("../FXML/categoriesPage.fxml"));
        Stage window = (Stage) proPic.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void save() throws IOException {
        if (!bio.getText().equals("")){
            currentUser.setInfo(bio.getText());
            bio.setText("");
        }
        if (!email.getText().equals("")){
            currentUser.setEmail(email.getText());
            email.setText("");
        }
        if (!firstName.getText().equals("")){
            currentUser.setFirstName(firstName.getText());
            firstName.setText("");
        }
        if (!lastName.getText().equals("")){
            currentUser.setLastName(lastName.getText());
            lastName.setText("");
        }
        if (!phoneNumber.getText().equals("")){
            currentUser.setPhoneNumber(phoneNumber.getText());
            phoneNumber.setText("");
        }
        clientManager.sendUsers("SAVE_SETTINGS");
        clientManager.sendClicked(gson.toJson(currentUser));
        loadData();
    }

    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "MainHub")));
        Stage window = (Stage) proPic.getScene().getWindow();
        window.setScene(new Scene(root));
    }
}
