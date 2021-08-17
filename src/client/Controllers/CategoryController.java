package client.Controllers;

import client.utils.Manager;
import client.Models.User;
import client.network.ClientManager;
import client.shared.UpdateCategoriesResponse;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import client.Config;
import client.utils.ChangeScene;
import client.utils.LoadComponent;

import java.io.IOException;
import java.util.LinkedList;

public class CategoryController {
    LinkedList<LinkedList<String>> added = new LinkedList<>();
    ClientManager clientManager;
    User currentUser;
    Gson gson;
    @FXML
    private AnchorPane Salam;

    @FXML
    private GridPane grid;

    @FXML
    private Pane overlay;

    @FXML
    private TextField groupName;

    @FXML
    private Button closeOverlay;

    @FXML
    private Button createGroup;

    @FXML
    private GridPane overlayGrid;

    @FXML
    void back() throws IOException {
        new ChangeScene(Config.getConfig("mainConfig").getProperty((String.class), "Sample"),grid);
    }

    public void initialize() throws IOException {
        this.clientManager = Manager.getClientManager();
        this.currentUser = Manager.getUser();
        this.gson = new Gson();
        loadData();
    }

    public void loadData() throws IOException {
        grid.getChildren().clear();
        LinkedList<LinkedList<String>> catg = currentUser.getCategories();
        for (LinkedList<String> cat : catg) {
            if (cat.size() != 0) {
                LoadComponent loadComponent = new LoadComponent("../FXML/FollowersComponent.fxml");
                AnchorPane anchorPane = loadComponent.loadAnchor();
                FollowerComponentController item = loadComponent.loadFxml().getController();
                item.setName(cat.get(0));
                item.getPane().setOnMouseClicked(e -> {
                    try {
                        loadFollowers(2,cat);
                        overlay.setVisible(true);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                grid.add(anchorPane, 1, grid.getRowCount() + 1);
                grid.setLayoutX(-150);
                grid.setLayoutY(-25);
                GridPane.setMargin(anchorPane, new Insets(-25));

            }
        }
        loadFollowers(1,null);
    }

    public void loadFollowers(int type,LinkedList<String> catg) throws IOException {
        overlayGrid.getChildren().clear();
        LinkedList<String> followers;
        if (type == 1) {
             followers = currentUser.getFollowers();
        } else {
             int index = currentUser.getCategories().indexOf(catg);
             followers = currentUser.getCategories().get(index);
//             followers.remove(0);
        }
        int i = 0;
        for (String str : followers){
            if (i>0) {
                LoadComponent loadComponent = new LoadComponent("../FXML/FollowersComponent.fxml");
                AnchorPane anchorPane = loadComponent.loadAnchor();
                FollowerComponentController item = loadComponent.loadFxml().getController();
//            item.getDeleteSign().setVisible(true);
//            item.getDeleteSign().setOnMouseClicked(e -> deleteFromCatg(str));
                item.setName("@" + str);
                if (type == 1) {
                    item.getPane().setOnMouseClicked(e -> chooseFlwr(item, str));
                } else {
                    item.getDeleteSign().setVisible(true);
                    item.getDeleteSign().setOnMouseClicked(e -> {
                        try {
                            deleteFromCatg(followers, str);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                overlayGrid.add(anchorPane, 1, overlayGrid.getRowCount() + 1);
                overlayGrid.setLayoutX(-150);
                overlayGrid.setLayoutY(-25);
                GridPane.setMargin(anchorPane, new Insets(-25));
            }
            i++;
        }
    }

    private void deleteFromCatg(LinkedList<String> followers,String str) throws IOException {
        UpdateCategoriesResponse up = new UpdateCategoriesResponse(str,followers);
        clientManager.sendUsers("UPDATE_CATEGORIES");
        clientManager.sendClicked(gson.toJson(up));
//        Users.updateCategories(followers,str);
        overlayGrid.getChildren().clear();
        loadFollowers(2,followers);
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

    public void createGroup() throws IOException {
        if (groupName.getText() != null && members.size() != 0){
            members.add(0,groupName.getText());
            clientManager.sendUsers("CREATE_CATEGORIES");
            UpdateCategoriesResponse up = new UpdateCategoriesResponse(currentUser.getUsername(),members);
            clientManager.sendClicked(gson.toJson(up));
//            Users.createCatg(currentUser,members);
            added.add(members);
            grid.getChildren().clear();
            loadData();
            closeOverlay();
        }
    }


    @FXML
    void closeOverlay() {
        overlay.setVisible(false);
    }

    @FXML
    void createCatg() throws IOException {
        overlay.setVisible(true);
        loadFollowers(1,null);
        members.clear();
    }

}
