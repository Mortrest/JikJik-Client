package client.Controllers;

import client.Config;
import client.Manager;
import client.Models.User;
import client.network.ClientManager;
import client.shared.NotifResponse;
import client.shared.RoomResponse;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class MainHub {
    private Gson gson;
    private ClientManager clientManager;
    private User currentUser;
    private String whichPage;
    @FXML
    private AnchorPane mainScene;
    @FXML
    void refresh() {
    }

    public void initialize() throws IOException {
        this.gson = new Gson();
        this.currentUser = Manager.getUser();
        this.clientManager = Manager.getClientManager();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    clientManager.sendClicked("MSG");
                    clientManager.sendClicked("DATA");
                    RoomResponse rs = gson.fromJson(clientManager.read(), RoomResponse.class);
                    clientManager.sendClicked("NOTIF");
                    NotifResponse notifResponse = gson.fromJson(clientManager.read(), NotifResponse.class);
                    Manager.setNotifs(notifResponse.getNotifs());
                    Manager.setRooms(rs.getRooms());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        loadHome();
    }

    public void loadHome() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Home")))));
        eachTweet();
    }

    private void eachTweet() throws IOException {
        clientManager.sendTweets("GET_TWEET_ID");
        String ID = clientManager.read();
        if (!ID.equals("NULL")){
            mainScene.getChildren().setAll((Node) FXMLLoader.
                    load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "EachTweet")))));
        }
    }

    public void loadExplore() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Explore")))));
        eachTweet();

    }

    public void loadNotif() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Notifs")))));

    }

    public void loadRooms() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Rooms")))));

    }
    public void loadProfile() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Profile")))));
        eachTweet();
    }
    public void loadSettings() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Settings")))));
    }

}
