package client.Controllers;

import client.Config;
import client.utils.Manager;
import client.Models.Chat;
import client.Models.Room;
import client.Models.User;
import client.network.ClientManager;
import client.shared.CreateGroupResponse;
import client.shared.ShowChatResponse;
import client.utils.ChangeScene;
import client.utils.LoadComponent;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class RoomsController {
    ClientManager clientManager;
    LinkedList<String> followers;
    LinkedList<LinkedList<String>> catg;
    LinkedList<Room> tw;
    Gson gson;
    boolean isRunning;
    User currentUser;
    @FXML
    public TextField groupName;
    @FXML
    public Button closeOverlay;
    @FXML
    public Button createGroup;
    @FXML
    public Pane overlay;
    @FXML
    private GridPane grid, overlayGrid;

    public void initialize() throws IOException {
        this.gson = new Gson();
        this.currentUser = Manager.getUser();
        this.clientManager = Manager.getClientManager();
        this.isRunning = true;
//        clientManager.sendClicked("MSG");
//        String str = clientManager.read();
//        RoomResponse rs = gson.fromJson(str, RoomResponse.class);
//        this.tw = rs.getRooms();
//        loadData();
//        Thread thread = new Thread(() -> {
//            while (isRunning) {
//                //                    clientManager.sendClicked("MSG");
////                    clientManager.sendClicked("DATA");
////                    RoomResponse rs = gson.fromJson(clientManager.read(), RoomResponse.class);
//                this.tw = Manager.getRooms();
//                try {
//                    loadData();
//                    Thread.sleep(1000);
//                } catch (InterruptedException | IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        thread.start();
        this.tw = Manager.getRooms();
        loadData();

    }

    public void loadFollowers() throws IOException {
        this.followers = currentUser.getFollowers();
        for (String str : followers) {
            LoadComponent loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "FollowersComponent"));
            AnchorPane anchorPane = loadComponent.loadAnchor();
            FollowerComponentController item = loadComponent.loadFxml().getController();
            item.setName("@" + str);
            item.getPane().setOnMouseClicked(e -> chooseFlwr(item, str));
            overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
            overlayGrid.setLayoutX(-150);
            overlayGrid.setLayoutY(-25);
            GridPane.setMargin(anchorPane, new Insets(-25));
        }
        this.catg = currentUser.getCategories();
        for (int i = 0; i < catg.size(); i++) {
            LoadComponent loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "FollowersComponent"));
            AnchorPane anchorPane = loadComponent.loadAnchor();
            FollowerComponentController item = loadComponent.loadFxml().getController();
            if (catg.get(i).size() >= 2) {
                item.setName("Category: " + catg.get(i).get(0));
                int finalI = i;
//                catg.get(i).remove(0);
                item.getPane().setOnMouseClicked(e -> chooseCatg(item, catg.get(finalI)));
                overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
                overlayGrid.setLayoutX(-150);
                overlayGrid.setLayoutY(-25);
                GridPane.setMargin(anchorPane, new Insets(-25));
            }
        }
    }

    public void chatPage(String id) throws IOException {
        if (clientManager.getConnected()) {
//        Chats.setRoomID(id);
            clientManager.sendChats(id);
            isRunning = false;
            new ChangeScene("../FXML/chat.fxml", grid);
        } else {
            if (Manager.roomStuffMap.containsKey(id)){
                Manager.setRoomID(id);
                isRunning = false;
                new ChangeScene("../FXML/chat.fxml", grid);
            }
        }
    }

    public void closeOverlay() {
        overlay.setVisible(false);
    }

    public void openOverlay() {
        overlay.setVisible(true);
    }

    LinkedList<Room> groups = new LinkedList<>();

    public void createGroup() throws IOException {
        if (groupName.getText() != null && members.size() != 0 && clientManager.getConnected()) {
            clientManager.sendClicked("MSG");
            clientManager.sendClicked("SPEC");
            clientManager.sendClicked(groupName.getText());
            String str = clientManager.read();
            if (!str.equals("NULL")) {
                Room room = gson.fromJson(str, Room.class);
                if (room.getMembers() != null) {
                    for (String mem : members) {
                        if (!room.getMembers().contains(mem)) {
                            room.getMembers().add(mem);
                        }
                    }
                }
            } else {
                members.add(currentUser.getUsername());
                clientManager.sendClicked("MSG");
                clientManager.sendClicked("CREATE_GROUP");
                CreateGroupResponse cr = new CreateGroupResponse(members, groupName.getText());
                clientManager.sendClicked(gson.toJson(cr));
//                Chats.creatGroupRoom(members, groupName.getText());
            }
            clientManager.sendChats("SAVE");
            grid.getChildren().clear();
            loadData();
            closeOverlay();
        }
    }

    public void loadData() throws IOException {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            grid.setLayoutY(40);
//        LinkedList<Room> tw = Chats.userRoom(currentUser.getUsername());
            for (int i = 0; i < tw.size(); i++) {
                LoadComponent loadComponent = null;
                try {
                    loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "RoomProfileComponent"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AnchorPane anchorPane = loadComponent.loadAnchor();
                RoomProfileComponentController itemController = loadComponent.loadFxml().getController();
                int finalI = i;
                itemController.getPane().setOnMouseClicked(e -> {
                    try {
                        chatPage(tw.get(finalI).getRoomID());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                if (tw.get(i).getType().equals("pv")) {
                    if (tw.get(i).getOwner1().equals(currentUser.getUsername())) {
                        itemController.setNameLabel(tw.get(i).getOwner2());
                        itemController.getUnread().setText(Integer.toString(tw.get(i).getUnread1()));
                        if (clientManager.getConnected()) {
                            User target = null;
                            try {
                                target = Manager.getUser(tw.get(i).getOwner2());
                            } catch (IOException e) {
                                System.out.println("nis ke");
                                e.printStackTrace();
                            }
                            if (Objects.requireNonNull(target).getProfilePic() != null) {
                                itemController.getProfilePicture().setFill(new ImagePattern(new Image(target.getProfilePic())));
                            }
                        }

                    } else {
                        itemController.setNameLabel(tw.get(i).getOwner1());
                        itemController.getUnread().setText(Integer.toString(tw.get(i).getUnread2()));
                        if (clientManager.getConnected()) {
                            User target = null;
                            try {
                                target = Manager.getUser(tw.get(i).getOwner1());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (target.getProfilePic() != null) {
                                itemController.getProfilePicture().setFill(new ImagePattern(new Image(target.getProfilePic())));
                            }
                        }
                    }
                } else {
                    itemController.getUnread().setVisible(false);
                    itemController.setNameLabel(tw.get(i).getGroupName());
                }
//            Users.getChats().showChats(tw.get(i).getRoomID());
                if (1 == 2) {
                    LinkedList<Chat> chat = null;
                    try {
                        chat = getUserChat(tw.get(i).getRoomID());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (chat.size() != 0) {
                        if (chat.get(chat.size() - 1).getImage() == null) {
                            itemController.setLastMsg(chat.get(chat.size() - 1).getText());
                        } else {
                            itemController.setLastMsg("Photo");
                        }
                    } else {
                        itemController.setLastMsg("No Messages!");
                    }
                } else {
                    itemController.setLastMsg("You're Offline");
                }
                grid.add(anchorPane, 1, grid.getRowCount() + 1);
                grid.setLayoutY(grid.getLayoutY() - 4);
                grid.setLayoutX(-60);
//            GridPane.setMargin(anchorPane, new Insets(10));
            }
            try {
                loadFollowers();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        members.clear();
        });

    }

    private LinkedList<Chat> getUserChat(String roomID) throws IOException {
        clientManager.sendClicked("MSG");
        clientManager.sendClicked("GET_CHATS");
        clientManager.sendClicked(roomID);
        String str = clientManager.read();
        ShowChatResponse sr = gson.fromJson(str, ShowChatResponse.class);
        return sr.getChats();
    }

    public void refresh() throws IOException {
        loadData();
    }

    LinkedList<String> members = new LinkedList<>();

    public void chooseFlwr(FollowerComponentController item, String str) {
        if (item.rect1.isVisible()) {
            item.rect1.setVisible(false);
            members.remove(str);
        } else {
            item.rect1.setVisible(true);
            members.add(str);
        }
    }

    public void chooseCatg(FollowerComponentController item, LinkedList<String> str) {
        if (item.rect1.isVisible()) {
            item.rect1.setVisible(false);
            members.removeAll(str);
        } else {
            item.rect1.setVisible(true);
            members.addAll(str);
        }
    }
}