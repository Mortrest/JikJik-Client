//package client.Controllers;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.paint.ImagePattern;
//import javafx.scene.shape.Circle;
//import javafx.stage.Stage;
//import client.Config;
//import client.Logic.Users;
//import client.utils.ChangeProfilePicture;
//
//import java.io.IOException;
//
//public class EditProfileController {
//    @FXML
//    private AnchorPane Salam;
//
//    @FXML
//    private Label wrong;
//
//    @FXML
//    private TextField bio;
//
//    @FXML
//    private TextField phoneNumber;
//
//    @FXML
//    private Circle proPic;
//
//    @FXML
//    private TextField email;
//
//    @FXML
//    private TextField firstName;
//    @FXML
//    private TextField lastName;
//
//    public void initialize(){
//        loadData();
//    }
//
//    public void loadData(){
//        bio.setPromptText(Users.getCurrentUser().getInfo() == null ? "No Bio!" : Users.getCurrentUser().getInfo());
//        email.setPromptText(Users.getCurrentUser().getEmail() == null ? "No Email!" : Users.getCurrentUser().getEmail());
//        firstName.setPromptText(Users.getCurrentUser().getFirstName() == null ? "No First Name!" : Users.getCurrentUser().getFirstName());
//        lastName.setPromptText(Users.getCurrentUser().getLastName() == null ? "No Last Name!" : Users.getCurrentUser().getLastName());
//        phoneNumber.setPromptText(Users.getCurrentUser().getPhoneNumber() == null ? "No Phone number!" : Users.getCurrentUser().getPhoneNumber());
//        if (Users.getCurrentUser().getProfilePic() != null){
//            Image image = new Image(Users.getCurrentUser().getProfilePic());
//            proPic.setFill(new ImagePattern(image));
//        }
//    }
//
//    public void changeBio() {
//        Users.getCurrentUser().setInfo(bio.getText());
//        bio.setPromptText(bio.getText());
//        Users.save();
//    }
//
//    public void changeEmail() {
//        Users.getCurrentUser().setEmail(email.getText());
//        email.setPromptText(email.getText());
//        Users.save();
//
//    }
//
//    public void changeFName() {
//        Users.getCurrentUser().setFirstName(firstName.getText());
//        firstName.setPromptText(firstName.getText());
//        Users.save();
//
//    }
//
//
//    public void changeLName() {
//        Users.getCurrentUser().setLastName(lastName.getText());
//        lastName.setPromptText(lastName.getText());
//        Users.save();
//
//
//    }
//
//
//    public void changePhoneNumber() {
//        Users.getCurrentUser().setPhoneNumber(phoneNumber.getText());
//        phoneNumber.setPromptText(phoneNumber.getText());
//        Users.save();
//
//    }
//
//
//    public void changeProfile() throws IOException {
//       new ChangeProfilePicture(bio);
//       Image image = new Image(Users.getCurrentUser().getProfilePic());
//       proPic.setFill(new ImagePattern(image));
//    }
//
//    public void categories() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        Parent root = fxmlLoader.load(getClass().getResource("../FXML/categoriesPage.fxml"));
//        Stage window = (Stage) proPic.getScene().getWindow();
//        window.setScene(new Scene(root));
//
//    }
//
//    public void save(){
//        if (bio.getText() != null){
//            Users.getCurrentUser().setInfo(bio.getText());
//        }
//        if (email.getText() != null){
//            Users.getCurrentUser().setEmail(email.getText());
//        }
//        if (firstName.getText() != null){
//            Users.getCurrentUser().setFirstName(firstName.getText());
//        }
//        if (lastName.getText() != null){
//            Users.getCurrentUser().setLastName(lastName.getText());
//        }
//        if (phoneNumber.getText() != null){
//            Users.getCurrentUser().setPhoneNumber(phoneNumber.getText());
//        }
//        loadData();
//    }
//
//    public void back() throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        Parent root = fxmlLoader.load(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Sample")));
//        Stage window = (Stage) proPic.getScene().getWindow();
//        window.setScene(new Scene(root));
//
//    }
//}
