package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sample.Models.Users;
import sample.utils.ChangeScene;

import java.io.IOException;

public class SignUpController {

    @FXML
    private AnchorPane login;

    @FXML
    private Pane pne3;

    @FXML
    private PasswordField signInPassword;

    @FXML
    private TextField signInTextField;

    @FXML
    private TextField email;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private Button signInBtn;

    @FXML
    private Label incorrect;

    @FXML
    void SignIn() throws IOException {

        if (!fName.getText().equals("") && !lName.getText().equals("") && !email.getText().equals("") && !signInPassword.getText().equals("") && !signInTextField.getText().equals("")){
            if (Users.searchUsername(signInTextField.getText()) == null) {
                Users.signUp(signInTextField.getText(),fName.getText(),lName.getText(),email.getText(),signInPassword.getText());
                new ChangeScene("../FXML/SignIn.fxml",login);
            }
        }else {
            incorrect.setVisible(true);
        }
    }

}
