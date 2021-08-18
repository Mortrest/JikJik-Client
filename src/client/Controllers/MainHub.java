package client.Controllers;

import client.Config;
import client.Models.User;
import client.network.ClientManager;
import client.shared.NotifResponse;
import client.shared.RoomResponse;
import client.shared.ShowTweetResponse;
import client.utils.Manager;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class MainHub {
    public ImageView offline;
    private Gson gson;
    private ClientManager clientManager;
    private User currentUser;
    private String whichPage;
//    LinkedList<String> Manager.pages;
    @FXML
    private AnchorPane mainScene;

    @FXML
    void refresh() {
    }

    public void initialize() {
//        this.Manager.pages = new LinkedList<>();
        this.gson = new Gson();
        this.currentUser = Manager.getUser();
        this.clientManager = Manager.getClientManager();
        Thread thread = new Thread(() -> {
            while (true) {
                if (clientManager.getConnected()) {
                    offline.setVisible(false);
                    try {
                        clientManager.sendClicked("MSG");
                        clientManager.sendClicked("DATA");
                        String read = clientManager.read();
                        RoomResponse rs = null;
                        if (read != null) {
                            try {
                                rs = gson.fromJson(read, RoomResponse.class);
                            } catch (Exception e) {
                                System.out.println(e);
                                System.out.println(read);
                            }
                        }
                        clientManager.sendClicked("NOTIF");
                        read = clientManager.read();
                        if (read != null) {
                            NotifResponse notifResponse = gson.fromJson(read, NotifResponse.class);
                            Manager.setNotifs(notifResponse.getNotifs());
                        }
                        clientManager.sendClicked("TW");
                        read = clientManager.read();
                        if (read != null){
                            ShowTweetResponse st = gson.fromJson(read,ShowTweetResponse.class);
                            Manager.setHomeTweets(st.getHomeTweets());
                            Manager.setExploreTweets(st.getExploreTweets());
                            Manager.setProfileTweets(st.getProfileTweets());
                        }
                        assert rs != null;
                        Manager.setRooms(rs.getRooms());
                        if (Manager.getTweetID() != null) {
                            tweetPage();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    offline.setVisible(true);
                }
                try {
                    Thread.sleep(2347);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            loadHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tweetPage() throws IOException {
        clientManager.sendTweets("SET_TWEET_ID");
        clientManager.sendClicked(Manager.getTweetID());
        Manager.setTweetID(null);
        eachTweet();
    }

    public void loadHome() throws IOException {
        Manager.pages.add("home");
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Home")))));
        eachTweet();
    }

    private void eachTweet() throws IOException {
        // Is it right?
        if (clientManager.getConnected()) {
            clientManager.sendTweets("GET_TWEET_ID");
            String ID = clientManager.read();
            if (!ID.equals("NULL")) {
                mainScene.getChildren().setAll((Node) FXMLLoader.
                        load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "EachTweet")))));
            }
        }
    }

    public void loadExplore() throws IOException {
        Manager.pages.add("explore");
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Explore")))));
        eachTweet();
    }

    public void loadNotif() throws IOException {
        Manager.pages.add("notif");
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Notifs")))));
    }

    public void loadRooms() throws IOException {
        Manager.pages.add("rooms");
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Rooms")))));
    }

    public void loadProfile() throws IOException {
        Manager.pages.add("profile");
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Profile")))));
        eachTweet();
    }

    public void loadSettings() throws IOException {
        mainScene.getChildren().setAll((Node) FXMLLoader.
                load(Objects.requireNonNull(getClass().getResource(Config.getConfig("mainConfig").getProperty((String.class), "Settings")))));
    }

}
