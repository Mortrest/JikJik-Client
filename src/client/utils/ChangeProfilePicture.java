package client.utils;

import client.Manager;
import client.Models.User;
import client.network.ClientManager;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class ChangeProfilePicture {
    User currentUser;
    ClientManager clientManager;

    public ChangeProfilePicture(TextField idPane) throws IOException {
        currentUser = Manager.getUser();
        clientManager = Manager.getClientManager();
        FileChooser fileChooser = new FileChooser();
        Stage window = (Stage) idPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        Random random = new Random();
        File file1 = new File("C:\\Users\\ali80\\Desktop\\Tokyo\\Phase2\\src\\sample\\images\\" + currentUser.getUsername() + random.nextInt(10000) + ".jpg");
        Files.copy(file.toPath(),file1.toPath());
        currentUser.setProfilePic(file1.toURI().toString());
        Manager.saveUser();
//        Users.save();
    }

    public ChangeProfilePicture(TextArea idPane, int type) throws IOException {
        currentUser = Manager.getUser();
        clientManager = Manager.getClientManager();
        FileChooser fileChooser = new FileChooser();
        Stage window = (Stage) idPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        Random random = new Random();
        File file1 = new File("C:\\Users\\ali80\\Desktop\\Tokyo\\Phase2\\src\\sample\\images\\" + random.nextInt(10000) + ".jpg");
        Files.copy(file.toPath(),file1.toPath());
        if (type == 1) {
            clientManager.sendTweets("SET_IMAGE");
            clientManager.sendClicked(file1.toPath().toString());

//            Tweets.setImage(file1.toPath().toString());
        } else if (type == 2){
            clientManager.sendChats("SET_IMAGE");
            clientManager.sendClicked(file1.toPath().toString());
//            Chats.setImage();
        }
    }
}
