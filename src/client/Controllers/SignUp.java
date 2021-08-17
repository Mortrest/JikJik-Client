package client.Controllers;

import client.shared.SignUpResponse;
import client.utils.ChangeScene;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static client.utils.Manager.clientManager;

public class SignUp {

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
        if (!fName.getText().equals("") && !lName.getText().equals("") && !email.getText().equals("") && !signInPassword.getText().equals("") && !signInTextField.getText().equals("")) {
            clientManager.sendClicked("SIGN_UP");
            SignUpResponse sr = new SignUpResponse(signInTextField.getText(),fName.getText(),lName.getText(),email.getText(),signInPassword.getText());
            Gson gson = new Gson();
            clientManager.sendClicked(gson.toJson(sr));
            String res = clientManager.read();
            if (res.equals("Signed Up")) {
                new ChangeScene("../FXML/signIn.fxml", signInBtn);
            } else {
                incorrect.setVisible(true);
            }
        }
    }
}
