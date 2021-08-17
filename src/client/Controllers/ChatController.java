package client.Controllers;

import client.Config;
import client.Models.Chat;
import client.Models.Room;
import client.Models.User;
import client.network.ClientManager;
import client.shared.MakeChatResponse;
import client.shared.ShowChatResponse;
import client.utils.ChangeScene;
import client.utils.ChangeProfilePicture;
import client.utils.Manager;
import com.google.gson.Gson;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
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
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ChatController {
    public ClientManager clientManager;
    public User currentUser;
    public Gson gson;
    public boolean isTimed;
    public FontAwesomeIconView timeOn;
    public FontAwesomeIconView timeOff;
    int time;
    public boolean isRunning;
    @FXML
    public TextArea overlayText;
    @FXML
    private DialogPane overlay;
    @FXML
    private Button backBtn, submit, closeBtn;
    @FXML
    private Circle profilePicture;
    @FXML
    private GridPane grid;
    @FXML
    private Label fName, lastSeen;
    @FXML
    private TextArea textArea;
    private Room room;

    public void initialize() throws IOException {
        overlay.setVisible(false);
        this.gson = new Gson();
        this.isRunning = true;
        this.time = 1;
        this.isTimed = false;
        this.currentUser = Manager.getUser();
        this.clientManager = Manager.getClientManager();
        Thread thread = new Thread(() -> {
            while (isRunning) {
                try {
                    Platform.runLater(() -> {

                        try {
                            loadData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
//        loadData();
    }

    public void back() throws IOException {
        overlay.setVisible(false);
        isRunning = false;
        clientManager.sendChats("NULL");
//        Chats.setRoomID(null);
        new ChangeScene("../FXML/mainHub.fxml", grid);
    }

    public void time() {
        isTimed = !isTimed;
        timeOn.setVisible(!timeOn.isVisible());
    }

    public void addChat() throws IOException {
//        Platform.runLater(() -> {
        //                clientManager.sendChats("GET_IMAGE");
        String imageStr = null;
        //                imageStr = clientManager.read();
        clientManager.sendChats("GET_ROOMID");
        String roomID = null;
        roomID = clientManager.read();
//        if (textArea.getText().charAt(0) == '@'){
//            textArea.setOnMouseClicked(e -> {
//                hyperlink(textArea);
//            });
//        }
        if (textArea.getText().equals("/left") && !room.getType().equals("PV")) {
            clientManager.sendChats("LEFT");
            clientManager.sendClicked(roomID);
            back();
        }
        if (textArea.getText().contains("/time")) {
            String a = textArea.getText().substring(6);
            clientManager.sendClicked("TIME");
            clientManager.sendClicked(a);
            this.time = Integer.parseInt(a);
        } else {
            if (imageStr != null) {
                File file = new File(imageStr);
                Image image = new Image(file.toURI().toString());
                MakeChatResponse mr = new MakeChatResponse(textArea.getText(), currentUser.getUsername(), roomID, image.getUrl(), true, isTimed);
                clientManager.sendChats("MAKE_CHAT");
                clientManager.sendClicked(gson.toJson(mr));
                //            Chats.makeImageChat(textArea.getText(), currentUser.getUsername(), Chats.getRoomID(), image.getUrl());
            } else {
                MakeChatResponse mr = new MakeChatResponse(textArea.getText(), currentUser.getUsername(), roomID, null, false, isTimed);
                clientManager.sendChats("MAKE_CHAT");
                clientManager.sendClicked(gson.toJson(mr));
                //            Chats.makeChat(textArea.getText(), currentUser.getUsername(), Chats.getRoomID());
            }
            textArea.setText("");
            clientManager.sendChats("IMAGE_NULL");
            //        Chats.setImage(null);
            grid.getChildren().clear();
            try {
                loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        });
        }
    }

    private void hyperlink(String text) throws IOException {
        if (text.contains("TWEET")){
            String fin = text.substring(6);
            clientManager.sendTweets("ELIGIBLE");
            clientManager.sendClicked(fin);
            if (clientManager.read().equals("YES")) {
                Manager.setTweetID(fin);
                back();
            }
        }
        if (text.contains("USER")){
            String fin = text.substring(5);
            User us = Manager.getUser(fin);
            if (us != null) {
                clientManager.sendUsers("SET_PROFILE");
                clientManager.sendClicked(gson.toJson(us));
                isRunning = false;
                new ChangeScene(Config.getConfig("mainConfig").getProperty((String.class), "Profile"), grid);
            }
        }
        if (text.contains("GP")){
            String fin = text.substring(3);

        }
    }

    public void loadData() throws IOException {
//        Platform.runLater(() -> {
        grid.getChildren().clear();
        clientManager.sendChats("GET_ROOMID");
        String rid = null;
        rid = clientManager.read();
        clientManager.sendChats("SEARCH_ROOM");
        clientManager.sendClicked(rid);
//            Room room = null;
        this.room = gson.fromJson(clientManager.read(), Room.class);
        if (room != null && room.getType().equals("pv")) {
            clientManager.sendChats("SEEN");
            clientManager.sendClicked(currentUser.getUsername());
            clientManager.sendClicked(room.getRoomID());
            if (room.getOwner1().equals(room.getOwner2())) {
                fName.setText("Saved Messages");
                if (currentUser.getProfilePic() != null) {
                    profilePicture.setFill(new ImagePattern(new Image(currentUser.getProfilePic())));
                }
            } else if (room.getOwner1().equals(currentUser.getUsername())) {
                User user = null;
                user = Manager.getUser(room.getOwner2());

                assert user != null;
                if (user.getProfilePic() == null) {
                    profilePicture.setVisible(true);
                } else {
                    profilePicture.setFill(new ImagePattern(new Image(user.getProfilePic())));
                }
                fName.setText(room.getOwner2());
            } else {
//                User user;
//                user = Manager.getUser(room.getOwner1());
//                if (user.getProfilePic() != null) {
//                    profilePicture.setFill(new ImagePattern(new Image(user.getProfilePic())));
//                }
                fName.setText(room.getOwner1());
            }
        } else if (room != null && room.getType().equals("gp")) {
            fName.setText(room.getGroupName());
            lastSeen.setText(room.getMembers().size() + " Members");
        }
        clientManager.sendChats("GET_ROOMID");
        String roomID = null;
        roomID = clientManager.read();
        clientManager.sendClicked("MSG");
        clientManager.sendClicked("GET_CHATS");
        clientManager.sendClicked(roomID);
        ShowChatResponse sr = null;
        sr = gson.fromJson(clientManager.read(), ShowChatResponse.class);
        assert sr != null;
        LinkedList<Chat> tw = sr.getChats();
        for (int i = 0; i < tw.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            boolean c = false;
            if (tw.get(i).getImage() == null) {
                fxmlLoader.setLocation(getClass().getResource("../FXML/chatComponent.fxml"));
            } else {
                fxmlLoader.setLocation(getClass().getResource("../FXML/ChatImageComponent.fxml"));
                c = true;
            }
            AnchorPane anchorPane = null;
            anchorPane = fxmlLoader.load();
            ChatComponentController itemController = fxmlLoader.getController();
            itemController.setName(tw.get(i).getOwner());
            User user = null;
            user = Manager.getUser(tw.get(i).getOwner());
            assert user != null;
            if (user.getProfilePic() != null) {
                itemController.getCircle().setFill(new ImagePattern(new Image(user.getProfilePic())));
            }
            int finalI = i;
            if (!c) {
                String text = tw.get(i).getText();
                if (text.charAt(0) == '@'){
                    itemController.getText().setStyle("-fx-text-fill:#1d99ff");
                    itemController.getText().setOnMouseClicked(e -> {
                        try {
                            hyperlink(text);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
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
//            if (tw.get(i).getOwner().equals(currentUser.getUsername())) {
//                ownChat(anchorPane, itemController);
//            } else {

            grid.add(anchorPane, 1, grid.getRowCount() + 1);grid.setLayoutX(-40);grid.setLayoutY(20);
            if (c) {
                GridPane.setMargin(anchorPane, new Insets(-15, 100, 100, -15));
            } else {
                GridPane.setMargin(anchorPane, new Insets(-15));
            }
        }
//        });

    }


    public void delete(String ID) throws IOException {
        clientManager.sendChats("DELETE_CHAT");
        clientManager.sendClicked(ID);
        loadData();
    }

    public void edit(String ID) throws IOException {
        clientManager.sendChats("GET_CHAT");
        clientManager.sendClicked(ID);
        Chat chat = gson.fromJson(clientManager.read(), Chat.class);
        overlayText.setText(chat.getText());
        clientManager.sendChats("EDIT_CHAT");
        clientManager.sendClicked(ID);
        overlay.setVisible(true);
    }


    public void editBtn() throws IOException {
        clientManager.sendChats("EDIT_ID");
        String id = clientManager.read();
        clientManager.sendChats("HANDLE_EDIT");
        clientManager.sendClicked(id);
        clientManager.sendClicked(overlayText.getText());
        closeOverlay();
        loadData();
    }

    public void attach() throws IOException {
        new ChangeProfilePicture(textArea, 2);
    }

    public void closeOverlay() throws IOException {
        overlay.setVisible(false);
        overlayText.setText("");
        clientManager.sendChats("EDIT_NULL");
    }


}