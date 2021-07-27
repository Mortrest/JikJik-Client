package client.Controllers;

import client.Manager;
import client.Models.Chat;
import client.Models.Room;
import client.Models.User;
import client.network.ClientManager;
import client.shared.MakeChatResponse;
import client.shared.ShowChatResponse;
import client.utils.ChangeProfilePicture;
import client.utils.ChangeScene;
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
                    Thread.sleep(1000);
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

    public void time(){
        isTimed = !isTimed;
        timeOn.setVisible(!timeOn.isVisible());
    }

    public void addChat() throws IOException {
//        Platform.runLater(() -> {
        //                clientManager.sendChats("GET_IMAGE");
        String imageStr = null;
        //                imageStr = clientManager.read();
        try {
            clientManager.sendChats("GET_ROOMID");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String roomID = null;
        try {
            roomID = clientManager.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (textArea.getText().equals("/left") && !room.getType().equals("PV")) {
            clientManager.sendChats("LEFT");
            clientManager.sendClicked(roomID);
            back();
        }
        if (textArea.getText().contains("/time")){
            String a = textArea.getText().substring(6);
            clientManager.sendClicked("TIME");
            clientManager.sendClicked(a);
            this.time = Integer.parseInt(a);
        }
        else {
            if (imageStr != null) {
                File file = new File(imageStr);
                Image image = new Image(file.toURI().toString());
                MakeChatResponse mr = new MakeChatResponse(textArea.getText(), currentUser.getUsername(), roomID, image.getUrl(), true,isTimed);
                try {
                    clientManager.sendChats("MAKE_CHAT");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    clientManager.sendClicked(gson.toJson(mr));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            Chats.makeImageChat(textArea.getText(), currentUser.getUsername(), Chats.getRoomID(), image.getUrl());
            } else {
                MakeChatResponse mr = new MakeChatResponse(textArea.getText(), currentUser.getUsername(), roomID, null, false,isTimed);
                try {
                    clientManager.sendChats("MAKE_CHAT");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    clientManager.sendClicked(gson.toJson(mr));
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            Chats.makeChat(textArea.getText(), currentUser.getUsername(), Chats.getRoomID());
            }
            textArea.setText("");
            try {
                clientManager.sendChats("IMAGE_NULL");
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void loadData() throws IOException {
//        Platform.runLater(() -> {

            grid.getChildren().clear();
            try {
                clientManager.sendChats("GET_ROOMID");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String rid = null;
            try {
                rid = clientManager.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                clientManager.sendChats("SEARCH_ROOM");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                clientManager.sendClicked(rid);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Room room = null;
            try {
                this.room = gson.fromJson(clientManager.read(), Room.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (room != null && room.getType().equals("pv")) {
                try {
                    clientManager.sendChats("SEEN");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    clientManager.sendClicked(currentUser.getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    clientManager.sendClicked(room.getRoomID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
//            Chats.seen(currentUser.getUsername(),room.getRoomID());
                if (room.getOwner1().equals(room.getOwner2())) {
                    fName.setText("Saved Messages");
                    if (currentUser.getProfilePic() != null) {
                        profilePicture.setFill(new ImagePattern(new Image(currentUser.getProfilePic())));
                    }
                } else if (room.getOwner1().equals(currentUser.getUsername())) {
                    User user = null;
                    try {
                        user = Manager.getUser(room.getOwner2());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert user != null;
                    if (user.getProfilePic() == null) {
                        profilePicture.setVisible(true);
                    } else {
                        profilePicture.setFill(new ImagePattern(new Image(user.getProfilePic())));
                    }
                    fName.setText(room.getOwner2());

                } else {

                    User user = null;
                    try {
                        user = Manager.getUser(room.getOwner1());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (user.getProfilePic() != null) {
                        profilePicture.setFill(new ImagePattern(new Image(user.getProfilePic())));
                    }
                    fName.setText(room.getOwner1());
                }
            } else if (room != null && room.getType().equals("gp")) {
                fName.setText(room.getGroupName());
                lastSeen.setText(room.getMembers().size() + " Members");
            }
            try {
                clientManager.sendChats("GET_ROOMID");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String roomID = null;
            try {
                roomID = clientManager.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                clientManager.sendClicked("MSG");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                clientManager.sendClicked("GET_CHATS");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                clientManager.sendClicked(roomID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ShowChatResponse sr = null;
            try {
                sr = gson.fromJson(clientManager.read(), ShowChatResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                try {
                    anchorPane = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ChatComponentController itemController = fxmlLoader.getController();
                itemController.setName(tw.get(i).getOwner());
                User user = null;
                try {
                    user = Manager.getUser(tw.get(i).getOwner());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert user != null;
                if (user.getProfilePic() != null) {
                    itemController.getCircle().setFill(new ImagePattern(new Image(user.getProfilePic())));
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
//            if (tw.get(i).getOwner().equals(currentUser.getUsername())) {
//                ownChat(anchorPane, itemController);
//            } else {

                grid.add(anchorPane, 1, grid.getRowCount() + 1);
                grid.setLayoutX(-40);
                grid.setLayoutY(20);
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
//        Chats.deleteChat(ID);
        loadData();
    }

    public void edit(String ID) throws IOException {
        clientManager.sendChats("GET_CHAT");
        clientManager.sendClicked(ID);
        Chat chat = gson.fromJson(clientManager.read(), Chat.class);
        overlayText.setText(chat.getText());
        clientManager.sendChats("EDIT_CHAT");
        clientManager.sendClicked(ID);
//        Chats.setEditID(ID);
        overlay.setVisible(true);
    }


    public void editBtn() throws IOException {
        clientManager.sendChats("EDIT_ID");
        String id = clientManager.read();
        clientManager.sendChats("HANDLE_EDIT");
        clientManager.sendClicked(id);
        clientManager.sendClicked(overlayText.getText());
//        Chats.editChat(id, overlayText.getText());
        closeOverlay();
        loadData();
    }

    public void attach() throws IOException {
        new ChangeProfilePicture(textArea, 2);
    }

    public void closeOverlay() throws IOException {
        overlay.setVisible(false);
        overlayText.setText("");
//        Chats.setEditID(null);
        clientManager.sendChats("EDIT_NULL");
    }

    public Circle getProfilePicture() {
        return profilePicture;
    }


}