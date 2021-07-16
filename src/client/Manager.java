package client;

import client.Models.Chat;
import client.Models.Room;
import client.Models.User;
import client.network.ClientManager;
import com.google.gson.Gson;

import java.io.IOException;

public class Manager {
    static public ClientManager clientManager;
    static public String spectateName;
    static public String AuthToken;
    static public User user;
    static Gson gson;
    public Manager() throws IOException {
        gson = new Gson();
        user = null;
        clientManager = new ClientManager();
        clientManager.start();
    }

    public static String getAuthToken() {
        return AuthToken;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Manager.user = user;
    }

    public static User getUser(String username) throws IOException {
        clientManager.sendClicked("GET_USER");
        clientManager.sendClicked(username);
        return gson.fromJson(clientManager.read(), User.class);
    }

    public static Room getRoom(String str) throws IOException {
        clientManager.sendClicked("GET_ROOM");
        clientManager.sendClicked(str);
        return gson.fromJson(clientManager.read(), Room.class);

    }

    public static Chat getChat(String str) throws IOException {
        clientManager.sendClicked("GET_CHAT");
        clientManager.sendClicked(str);
        return gson.fromJson(clientManager.read(), Chat.class);

    }


    public static void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public static String getSpectateName() {
        return spectateName;
    }

    public static void setSpectateName(String spectateName) {
        Manager.spectateName = spectateName;
    }

    public static ClientManager getClientManager() {
        return clientManager;
    }
}
