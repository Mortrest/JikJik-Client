package sample.utils;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Logic.Chats;
import sample.Models.Tweets;
import sample.Models.Users;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class ChangeProfilePicture {
    public ChangeProfilePicture(TextField idPane) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Stage window = (Stage) idPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        Random random = new Random();
        File file1 = new File("C:\\Users\\ali80\\Desktop\\Tokyo\\Phase2\\src\\sample\\images\\" + Users.getCurrentUser().getUsername() + random.nextInt(10000) + ".jpg");
        Files.copy(file.toPath(),file1.toPath());
        Users.getCurrentUser().setProfilePic(file1.toURI().toString());
        Users.save();
    }

    public ChangeProfilePicture(TextArea idPane, int type) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Stage window = (Stage) idPane.getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);
        Random random = new Random();
        File file1 = new File("C:\\Users\\ali80\\Desktop\\Tokyo\\Phase2\\src\\sample\\images\\" + random.nextInt(10000) + ".jpg");
        Files.copy(file.toPath(),file1.toPath());
        if (type == 1) {
            Tweets.setImage(file1.toPath().toString());
        } else if (type == 2){
            Chats.setImage(file1.toPath().toString());
        }
    }
}
