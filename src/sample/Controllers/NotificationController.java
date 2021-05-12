package sample.Controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import sample.Models.Notif;
import sample.Models.Notifs;
import sample.Models.User;
import sample.Models.Users;
import sample.utils.LoadComponent;

import java.io.IOException;
import java.util.LinkedList;

public class NotificationController {

    @FXML
    private GridPane grid;

    public void initialize() throws IOException {
        loadData();
    }

    public void loadData() throws IOException{
        grid.getChildren().clear();
        LinkedList<Notif> tw = Users.getNotifs().showNotif(Users.getCurrentUser().getUsername());
        for (Notif notif : tw) {
            LoadComponent loadComponent = new LoadComponent("../FXML/NotificationComponent.fxml");
            AnchorPane anchorPane = loadComponent.loadAnchor();
            NotificationComponentController itemController = loadComponent.loadFxml().getController();
            User user = Users.searchUsername(notif.getOwner());
            assert user != null;
            itemController.setName("@" + user.getUsername() + " - " + user.getName());
            if (user.getProfilePic() != null){
                itemController.getProfilePicture().setFill(new ImagePattern(new Image(user.getProfilePic())));
            }
            itemController.setText(notif.getText());
            if (notif.getType().equals("1")) {
                itemController.hide();
            } else {
                itemController.getAccept().setOnAction(e -> {
                    try {
                        acceptReq(notif);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getDecline().setOnAction(e -> {
                    try {
                        declineReq(notif);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
                itemController.getMute().setOnAction(e -> {
                    try {
                        muteReq(notif);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });

            }
            grid.add(anchorPane, 1, grid.getRowCount() + 1);
            grid.setLayoutX(-20);
            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }

    public void acceptReq(Notif notif) throws IOException {
        Notifs.acceptOrDeclineReq(notif,1);
        loadData();
    }
    public void declineReq(Notif notif) throws IOException {
        Notifs.acceptOrDeclineReq(notif,2);
        loadData();
    }
    public void muteReq(Notif notif) throws IOException {
        Notifs.acceptOrDeclineReq(notif,3);
        loadData();
    }


}