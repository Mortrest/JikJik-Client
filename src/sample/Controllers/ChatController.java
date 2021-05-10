package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import sample.Models.*;
import sample.utils.ChangeProfilePicture;
import sample.utils.ChangeScene;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ChatController {
    @FXML
    public TextArea overlayText;
    @FXML
    private DialogPane overlay;
    @FXML
    private Button backBtn, submit, closeBtn;
    @FXML
    private GridPane grid;
    @FXML
    private Label fName, lastSeen;
    @FXML
    private TextArea textArea;

    public void initialize() throws IOException {
        overlay.setVisible(false);
        loadData();
    }

    public void back() throws IOException {
        overlay.setVisible(false);
        new ChangeScene("../FXML/sample.fxml", grid);
    }

    public void addChat() throws IOException {
        if (Chats.getImage() != null) {
            File file = new File(Chats.getImage());
            Image image = new Image(file.toURI().toString());
            Chats.makeImageChat(textArea.getText(), Users.getCurrentUser().getUsername(), Chats.getRoomID(), image.getUrl());
        } else {
            Chats.makeChat(textArea.getText(), Users.getCurrentUser().getUsername(), Chats.getRoomID());
        }
        textArea.setText("");
        Chats.setImage(null);
        grid.getChildren().clear();
        loadData();
    }

    public void ownChat(AnchorPane anchorPane, ChatComponentController item) {
        item.getCircle().setLayoutX(item.getCircle().getLayoutX() + 895);
        Label text = item.getText();
        Label name = item.getName();
        text.setLayoutX(text.getLayoutX() + 700);
        if (text.getLayoutX() + text.getText().length() * 4 > 878) {
            int len = text.getText().length() - "salam iman".length();
            text.setLayoutX(text.getLayoutX() - len * 4 - 10);
        }
        if (text.getLayoutX() + text.getText().length() * 4 < 878) {
            int len = text.getText().length() - "salam iman".length();
            text.setLayoutX(text.getLayoutX() - len * 4 + 10);
        }
        name.setLayoutX(name.getLayoutX() + 700);
        grid.add(anchorPane, 1, grid.getRowCount() + 1);
        grid.setLayoutX(-40);
    }

    public void loadData() throws IOException {
        grid.getChildren().clear();
        Room room = Chats.searchRoomID(Chats.getRoomID());
        if (room != null && room.getType().equals("pv")) {
            if (room.getOwner1().equals(room.getOwner2())){
                fName.setText("Saved Messages");
            }
            else if (room.getOwner1().equals(Users.getCurrentUser().getUsername())) {
                fName.setText(room.getOwner2());
            } else {
                fName.setText(room.getOwner1());
            }
        }

        LinkedList<Chat> tw = Users.getChats().showChats(Chats.getRoomID());
        for (int i = 0; i < tw.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            boolean c = false;
            if (tw.get(i).getImage() == null) {
                fxmlLoader.setLocation(getClass().getResource("../FXML/chatComponent.fxml"));
            } else {
                fxmlLoader.setLocation(getClass().getResource("../FXML/ChatImageComponent.fxml"));
                c = true;
            }
            AnchorPane anchorPane = fxmlLoader.load();
            ChatComponentController itemController = fxmlLoader.getController();
            itemController.setName(tw.get(i).getOwner());
            User user = Users.searchUsername(tw.get(i).getOwner());
            if (user.getProfilePic() != null){
                itemController.getCircle().setFill(new ImagePattern(new Image("/sample/images/ali.PNG")));
            }
            int finalI = i;
            if (!c) {
                itemController.setText(tw.get(i).getText());
                itemController.getEdit().setOnAction(e -> {
                    try {
                        edit(tw.get(finalI).getID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getDelete().setOnAction(e -> {
                    try {
                        delete(tw.get(finalI).getID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });

            } else {
                Image image = new Image(tw.get(i).getImage());
                itemController.getRectangle().setFill(new ImagePattern(image));

            }
//            if (tw.get(i).getOwner().equals(Users.getCurrentUser().getUsername())) {
//                ownChat(anchorPane, itemController);
//            } else {

            grid.add(anchorPane, 1, grid.getRowCount() + 1);
            grid.setLayoutX(-40);
            grid.setLayoutY(20);
            if (c) {
                GridPane.setMargin(anchorPane, new Insets(-15,100,100,-15));
            } else {
                GridPane.setMargin(anchorPane, new Insets(-15));
            }
        }
    }


    public void delete(String ID) throws IOException {
        Chats.deleteChat(ID);
        loadData();
    }

    public void edit(String ID) throws IOException {
        overlayText.setText(Chats.searchChat(ID).getText());
        Chats.setEditID(ID);
        overlay.setVisible(true);
    }


    public void editBtn() throws IOException {
        Chats.editChat(Chats.getEditID(), overlayText.getText());
        closeOverlay();
        loadData();
    }

    public void attach() throws IOException {
        new ChangeProfilePicture(textArea, 2);
    }

    public void closeOverlay() {
        overlay.setVisible(false);
        overlayText.setText("");
        Chats.setEditID(null);
    }

}