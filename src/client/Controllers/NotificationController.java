package client.Controllers;

import client.Config;
import client.utils.Manager;
import client.Models.Notif;
import client.Models.User;
import client.network.ClientManager;
import client.utils.LoadComponent;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;

import java.io.IOException;
import java.util.LinkedList;


public class NotificationController {
    ClientManager clientManager;
    LinkedList<Notif> notifs;
    User usera;
    Gson gson;
    @FXML
    private GridPane grid;

    public void initialize() throws IOException {
        gson = new Gson();
        clientManager = Manager.getClientManager();
        usera = Manager.getUser();
        Thread thread = new Thread(() -> {
            while (Manager.pages.getLast().equals("notif")) {
//                if (clientManager.getConnected()) {
                this.notifs = Manager.getNotifs();
                if (!Manager.pages.getLast().equals("notif")) {
                    break;
                }
                try {
                    loadData();
                    Thread.sleep(1590);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void loadData() throws IOException {
        Platform.runLater(() -> {
            grid.getChildren().clear();
            for (Notif notif : notifs) {
                LoadComponent loadComponent = null;
                try {
                    loadComponent = new LoadComponent(Config.getConfig("mainConfig").getProperty((String.class), "NotificationComponent"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AnchorPane anchorPane = loadComponent.loadAnchor();
                NotificationComponentController itemController = loadComponent.loadFxml().getController();

                if (clientManager.getConnected()) {
                    User user = null;
                    try {
                        user = Manager.getUser(notif.getOwner());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    assert user != null;
                    itemController.setName("@" + user.getUsername() + " - " + user.getName());
                    if (user.getProfilePic() != null) {
                        itemController.getProfilePicture().setFill(new ImagePattern(new Image(user.getProfilePic())));
                    }
                } else {
                    itemController.setName("@" + notif.getOwner());
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
        });
    }

    private void muteReq(Notif notif) throws IOException {
        sendNotif(notif, 3);
        loadData();
    }

    private void acceptReq(Notif notif) throws IOException {
        sendNotif(notif, 1);
        loadData();
    }

    private void declineReq(Notif notif) throws IOException {
        sendNotif(notif, 2);
        loadData();
    }

    public void sendNotif(Notif notif, int type) throws IOException {
        clientManager.sendClicked("DO_NOTIF");
        clientManager.sendClicked(gson.toJson(notif));
        clientManager.sendClicked(String.valueOf(type));
    }
}