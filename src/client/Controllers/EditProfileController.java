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

    public void changeBio() throws IOException {
        currentUser.setInfo(bio.getText());
        bio.setPromptText(bio.getText());
//        Users.save();
        Manager.saveUser();
    }

    public void changeEmail() throws IOException {
        currentUser.setEmail(email.getText());
        email.setPromptText(email.getText());
        Manager.saveUser();

    }

    public void changeFName() throws IOException {
        currentUser.setFirstName(firstName.getText());
        firstName.setPromptText(firstName.getText());
        Manager.saveUser();

    }


    public void changeLName() throws IOException {
        currentUser.setLastName(lastName.getText());
        lastName.setPromptText(lastName.getText());
        Manager.saveUser();
    }


    public void changePhoneNumber() throws IOException {
        currentUser.setPhoneNumber(phoneNumber.getText());
        phoneNumber.setPromptText(phoneNumber.getText());
        Manager.saveUser();
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

    public void save(){
        if (bio.getText() != null){
            currentUser.setInfo(bio.getText());
        }
        if (email.getText() != null){
            currentUser.setEmail(email.getText());
        }
        if (firstName.getText() != null){
            currentUser.setFirstName(firstName.getText());
        }
        if (lastName.getText() != null){
            currentUser.setLastName(lastName.getText());
        }
        if (phoneNumber.getText() != null){
            currentUser.setPhoneNumber(phoneNumber.getText());
        }
        loadData();
    }

    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Sample")));
        Stage window = (Stage) proPic.getScene().getWindow();
        window.setScene(new Scene(root));

    }
}
