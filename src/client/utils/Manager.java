package client.utils;

import client.Models.Chat;
import client.Models.Notif;
import client.Models.Room;
import client.Models.User;
import client.network.ClientManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.LinkedList;

public class Manager {
    static public ClientManager clientManager;
    static public String AuthToken;
    static public User user;
    static public LinkedList<Room> rooms;
    static public LinkedList<Notif> notifs;
    static public LinkedList<String> settingOffline;
    static public boolean isRunning;
    static public String tweetID;
    static Gson gson;
    public Manager() throws IOException {
        gson = new Gson();
        rooms = null;
        notifs = null;
        tweetID = null;
        isRunning = true;
        settingOffline = new LinkedList<>();
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

    public static LinkedList<Notif> getNotifs() {
        return notifs;
    }

    public static void setNotifs(LinkedList<Notif> notifs) {
        Manager.notifs = notifs;
    }

    public static LinkedList<Room> getRooms() {
        return rooms;
    }

    public static void addOffline(String str){
        settingOffline.add(str);
    }

    public static void setRooms(LinkedList<Room> rooms) {
        Manager.rooms = rooms;
    }

    public static void saveUser() throws IOException {
        clientManager.sendUsers("SAVE");
    }

    public static User getUser(String username) throws IOException {
        clientManager.sendClicked("GET_USER");
        clientManager.sendClicked(username);
        String a = clientManager.read();
        return gson.fromJson(a, User.class);
    }

    public static String getTweetID() {
        return tweetID;
    }

    public static void setTweetID(String tweetID) {
        Manager.tweetID = tweetID;
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

    public static ClientManager getClientManager() {
        return clientManager;
    }
}
