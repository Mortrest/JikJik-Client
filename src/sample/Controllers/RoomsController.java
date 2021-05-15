package sample.Controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import sample.Models.Chat;
import sample.Models.Chats;
import sample.Models.Room;
import sample.Models.Users;
import sample.utils.ChangeScene;
import sample.utils.LoadComponent;

import java.io.IOException;
import java.util.LinkedList;

public class RoomsController {
    @FXML
    public TextField groupName;
    @FXML
    public Button closeOverlay;
    @FXML
    public Button createGroup;
    @FXML
    public Pane overlay;
    @FXML
    private GridPane grid,overlayGrid;
    public void initialize() throws IOException {
        loadData();
    }

    public void loadFollowers() throws IOException {
        LinkedList<String> followers = Users.getCurrentUser().getFollowers();
        for (String str : followers){
            LoadComponent loadComponent = new LoadComponent("../FXML/FollowersComponent.fxml");
            AnchorPane anchorPane = loadComponent.loadAnchor();
            FollowerComponentController item = loadComponent.loadFxml().getController();
            item.setName("@"+str);
            item.getPane().setOnMouseClicked(e -> chooseFlwr(item,str));
            overlayGrid.add(anchorPane,1,overlayGrid.getRowCount()+1);
            overlayGrid.setLayoutX(-150);
            overlayGrid.setLayoutY(-25);
            GridPane.setMargin(anchorPane, new Insets(-25));
        }
    }

    public void chatPage(String id) throws IOException {
        Chats.setRoomID(id);
        new ChangeScene("../FXML/chat.fxml",grid);
    }

    public void closeOverlay(){
        overlay.setVisible(false);
    }

    public void openOverlay(){
        overlay.setVisible(true);
    }

    LinkedList<Room> groups = new LinkedList<>();


    public void createGroup() throws IOException {
        if (groupName.getText() != null && members.size() != 0){
            if (Chats.searchRoomID(Chats.searchGroup(groupName.getText())) != null){
                Room room = Chats.searchRoomID(Chats.searchGroup(groupName.getText()));
                for (String mem : members){
                    assert room != null;
                    if (!room.getMembers().contains(mem)){
                        room.getMembers().add(mem);
                    }
                }
                Chats.saveRooms();
            } else {
                members.add(Users.getCurrentUser().getUsername());
                Chats.creatGroupRoom(members, groupName.getText());
//                groups.add(room);
                Chats.saveRooms();

            }
            members.clear();
            grid.getChildren().clear();
            loadData();
            closeOverlay();
        }
    }

    public void loadData() throws IOException {
        grid.getChildren().clear();
        grid.setLayoutY(40);
        LinkedList<Room> tw = Chats.userRoom(Users.getCurrentUser().getUsername());
        System.out.println(tw);
//        tw.addAll(groups);
        for (int i = 0; i < tw.size(); i++) {
            LoadComponent loadComponent = new LoadComponent("../FXML/roomProfileComponent.fxml");
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
                if (tw.get(i).getOwner1().equals(Users.getCurrentUser().getUsername())) {
                    itemController.setNameLabel(tw.get(i).getOwner2());
                    if (Users.searchUsername(tw.get(i).getOwner2()).getProfilePic() != null){
                        itemController.getProfilePicture().setFill(new ImagePattern(new Image(Users.searchUsername(tw.get(i).getOwner2()).getProfilePic())));
                    }
                } else {
                    itemController.setNameLabel(tw.get(i).getOwner1());
                    if (Users.searchUsername(tw.get(i).getOwner1()).getProfilePic() != null){
                        itemController.getProfilePicture().setFill(new ImagePattern(new Image(Users.searchUsername(tw.get(i).getOwner1()).getProfilePic())));
                    }
                }
            }
            else {
                itemController.setNameLabel(tw.get(i).getGroupName());
            }
            LinkedList<Chat> chat = Users.getChats().showChats(tw.get(i).getRoomID());
            if (chat.size() != 0){
                if (chat.get(chat.size()-1).getImage() == null) {
                    itemController.setLastMsg(chat.get(chat.size() - 1).getText());
                } else {
                    itemController.setLastMsg("Photo");
                }
            } else {
                itemController.setLastMsg("No Messages!");
            }
            grid.add(anchorPane,1,grid.getRowCount()+1);
            grid.setLayoutY(grid.getLayoutY()-4);
            grid.setLayoutX(-60);
//            GridPane.setMargin(anchorPane, new Insets(10));
        }
        loadFollowers();
    }

    public void refresh() throws IOException {
        loadData();
    }

    LinkedList<String> members = new LinkedList<>();
    public void chooseFlwr(FollowerComponentController item,String str){
        if (item.rect1.isVisible()){
            item.rect1.setVisible(false);
            members.remove(str);
        } else {
            item.rect1.setVisible(true);
            members.add(str);
        }
    }
}