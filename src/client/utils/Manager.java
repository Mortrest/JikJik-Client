package client.utils;

import client.Models.*;
import client.network.ClientManager;
import client.shared.RoomStuff;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Manager {
    static public ClientManager clientManager;
    static public String AuthToken;
    static public User user;
    static public String roomID;
    static public String imageForChat;
    static public LinkedList<Room> rooms;
    static public LinkedList<Notif> notifs;
    static public LinkedList<Tweet> homeTweets;
    static public LinkedList<Tweet> exploreTweets;
    static public LinkedList<Tweet> profileTweets;
//    static public LinkedList<OffineChat> offlineChats;
    static public LinkedList<String> pages;
    static public Map<String, RoomStuff> roomStuffMap;
    static public LinkedList<String> settingOffline;
    static public LinkedList<String> offlineChats;
    static public boolean isRunning;
    static public String tweetID;
    static Gson gson;
    public Manager() throws IOException {
//        gson = new Gson();
//        rooms = null;
//        notifs = null;
//        tweetID = null;
//        isRunning = true;
//        settingOffline = new LinkedList<>();
//        offineChats = new LinkedList<>();
//
//        user = null;
//        clientManager = new ClientManager();
//        clientManager.start();
        gson = new Gson();
        rooms = null;
        roomID = null;
        pages = new LinkedList<>();
        offlineChats = new LinkedList<>();
        imageForChat = null;
        notifs = null;
        tweetID = null;
        isRunning = true;
        user = null;
        roomStuffMap = new HashMap<>();
        clientManager = new ClientManager();
        clientManager.start();
    }

    public static User getUser(String username) throws IOException {
        clientManager.sendClicked("GET_USER");
        clientManager.sendClicked(username);
        String a = clientManager.read();
        return gson.fromJson(a, User.class);
    }

    public static void addToRoomStuff(String roomID, RoomStuff rs){
        if (roomStuffMap.containsKey(roomID)){
            roomStuffMap.replace(roomID,rs);
        } else {
            roomStuffMap.put(roomID,rs);
        }
    }

    public static void addOffline(String str){
        settingOffline.add(str);
    }

    public static void saveUser() throws IOException {
        clientManager.sendUsers("SAVE");
    }

    public static Room getRoom(String str) throws IOException {
        clientManager.sendClicked("GET_ROOM");
        clientManager.sendClicked(str);
        return gson.fromJson(clientManager.read(), Room.class);
    }

    public static String getImageForChat() {
        return imageForChat;
    }

    public static void setImageForChat(String imageForChat) {
        Manager.imageForChat = imageForChat;
    }

    public static Chat getChat(String str) throws IOException {
        clientManager.sendClicked("GET_CHAT");
        clientManager.sendClicked(str);
        return gson.fromJson(clientManager.read(), Chat.class);
    }

    public static String getAuthToken() {
        return AuthToken;
    }

    public static User getUser() {
        return user;
    }

    public static LinkedList<Tweet> getHomeTweets() {
        return homeTweets;
    }

    public static void setHomeTweets(LinkedList<Tweet> homeTweets) {
        Manager.homeTweets = homeTweets;
    }

    public static LinkedList<Tweet> getExploreTweets() {
        return exploreTweets;
    }

    public static void setExploreTweets(LinkedList<Tweet> exploreTweets) {
        Manager.exploreTweets = exploreTweets;
    }

    public static String getRoomID() {
        return roomID;
    }

    public static void addToOfflineChats(String str){
        offlineChats.add(str);
    }

    public static void setRoomID(String roomID) {
        Manager.roomID = roomID;
    }

    public static LinkedList<Tweet> getProfileTweets() {
        return profileTweets;
    }

    public static void setProfileTweets(LinkedList<Tweet> profileTweets) {
        Manager.profileTweets = profileTweets;
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

    public static void setRooms(LinkedList<Room> rooms) {
        Manager.rooms = rooms;
    }

    public static String getTweetID() {
        return tweetID;
    }

    public static void setTweetID(String tweetID) {
        Manager.tweetID = tweetID;
    }

    public static void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public static ClientManager getClientManager() {
        return clientManager;
    }
}
