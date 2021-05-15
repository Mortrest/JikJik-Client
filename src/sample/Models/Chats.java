package sample.Models;

import sample.ModelLoader;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;


public class Chats {
    static LinkedList<Room> rooms;
    static LinkedList<Chat> chats;
    static String image;

    static String roomID;
    static ModelLoader ml;


    static String editID;
    public Chats(ModelLoader modelLoader) {
        this.ml = modelLoader;
        rooms = modelLoader.loadRooms();
        chats = modelLoader.loadChats();
    }

    public static Chat searchChat(String ID) {
        for (Chat ch : chats) {
            if (ch.getID().equals(ID)) {
                return ch;
            }
        }
        return null;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        Chats.image = image;
    }

    public static String getEditID() {
        return editID;
    }

    public static void setEditID(String editID) {
        Chats.editID = editID;
    }

    public static void creatGroupRoom(LinkedList<String> members,String groupName){
        Random random = new Random();
        Date date = new Date();
        String ID = Integer.toString(random.nextInt(100000));
        Room room = new Room(ID,Long.toString(date.getTime()),members,groupName);
        rooms.add(room);
        System.out.println(rooms.getLast().getMembers() + " Sa");
        ml.save(rooms,"Rooms");
    }

    public static void deleteChat(String ID) {
        chats.remove(searchChat(ID));
        ml.save(chats,"Chats");
    }

    public static void editChat(String ID, String text) {
        Chat chat = searchChat(ID);
        if (!chat.forwarded && chat.getOwner().equals(Users.getCurrentUser().getUsername())) {
            chat.text = text;
            chat.edited = true;
            ml.save(chats, "Chats");
        }
    }

    public static void createSavedMsg(String username) {
        Date date = new Date();
        Room room = new Room(Integer.toString(rooms.size() + 1), Long.toString(date.getTime()), username, username, 0, 0);
        rooms.add(room);
        System.out.println(rooms.get(0).getOwner1());
        ml.save(rooms,"Rooms");
    }

    public static LinkedList<Room> userRoom(String username) {
        LinkedList<Room> userRooms = new LinkedList<>();
        ml.loadRooms();
        System.out.println(rooms.getLast() + " " + rooms.getLast().getMembers());
        for (Room room : rooms) {
            if (room.getOwner1() != null) {
                if (room.getOwner1().equals(username) || room.getOwner2().equals(username)) {
                    userRooms.add(room);
                }
            } else {
                System.out.println(room.getMembers());
                if (room.getMembers().contains(username)){
                    userRooms.add(room);
                }
            }
        }
        System.out.println(userRooms + "user rooms");
        return userRooms;
    }

    public static String getRoomID() {
        return roomID;
    }

    public static void setRoomID(String roomID) {
        Chats.roomID = roomID;
    }


    public LinkedList<Chat> getChats() {
        return chats;
    }

    public static void saveChats(){
        ml.save(chats,"Chats");
    }
    public static void saveRooms(){
        ml.save(rooms,"Rooms");
    }

    // Searching rooms
    public static Room searchRoom(String o1, String o2) {
        for (Room room : rooms) {
            if (room.getType().equals("pv")) {
                if (room.getOwner1().equals(o1) && room.getOwner2().equals(o2)) {
                    return room;
                } else if (room.getOwner2().equals(o1) && room.getOwner1().equals(o2)) {
                    return room;
                }
            }
        }
        return null;
    }

    public static Room searchRoomID(String roomID) {
        for (Room room : rooms) {
            if (room.getRoomID().equals(roomID)) {
                return room;
            }
        }
        return null;
    }

    public static String searchGroup(String groupName){
        for (Room room : rooms){
            if (room.getType().equals("gp")){
                if (room.getGroupName().equals(groupName)){
                    System.out.println(room.getRoomID());
                    return room.getRoomID();
                }
            }
        }
        return null;
    }

    public static String searchMemberInGroup(String username){
        for (Room room : rooms){
            if (room.getType().equals("gp")){
                for (String member : room.getMembers()){
                    if (member.equals(username)){
                        return room.getRoomID();
                    }
                }
            }
        }
        return null;
    }


    public static void makeChat(String text, String owner, String roomID) {
        Date date = new Date();
        Random random = new Random();
        Chat chat = new Chat(Integer.toString(random.nextInt(100000)), roomID, text, Long.toString(date.getTime()), owner, false, false);
        chats.add(chat);
        try {
            Room room = searchRoomID(roomID);
            room.setUnread1(room.getUnread1() + 1);
            room.setUnread2(room.getUnread2() + 1);
        } catch (Exception e) {
            ml.log("Chats-" + "Cannot makeChat (makeChat method in Chats)");
        }
        ml.log("Chats-" + "Chat Created " + text);
        ml.save(chats,"Chats");
    }

    public static void makeImageChat(String text, String owner, String roomID,String image) {
        Date date = new Date();
        Random random = new Random();
        Chat chat = new Chat(Integer.toString(random.nextInt(100000)), roomID, text, Long.toString(date.getTime()), owner, false, false,image);
        chats.add(chat);
        try {
            Room room = searchRoomID(roomID);
            room.setUnread1(room.getUnread1() + 1);
            room.setUnread2(room.getUnread2() + 1);
        } catch (Exception e) {
            ml.log("Chats-" + "Cannot makeChat (makeChat method in Chats)");
        }
        ml.log("Chats-" + "Chat Created " + text);
        ml.save(chats,"Chats");
    }

    public static void seen(String currentUser, String RoomID) {
        Room room = searchRoomID(RoomID);
        if (room.getOwner1().equals(currentUser)) {
            room.setUnread1(0);
        } else if (room.getOwner2().equals(currentUser)) {
            room.setUnread2(0);
        }
        ml.save(rooms,"Rooms");
    }

    // Making rooms
    public static String makeRoom(String o1, String o2) {
        Date date = new Date();
        Random random = new Random();
        Room room = new Room(Integer.toString(random.nextInt(100000)), Long.toString(date.getTime()), o1, o2, 0, 0);
        rooms.add(room);
        ml.save(rooms,"Rooms");
        ml.log("Chats-" + "Room created " + room.getRoomID());
        return room.getRoomID();
    }

    // ُShowing Comments
    public LinkedList<Chat> showChats(String roomID) {
        LinkedList<Chat> tw = new LinkedList<>();
        for (Chat t : chats) {
            if (t.getRoomID().equals(roomID)) {
                tw.add(t);
            }
        }
        tw = sortByDate(tw);
        return tw;
    }

    // Sorting by date
    public LinkedList<Chat> sortByDate(LinkedList<Chat> chat) {
        for (int i = 0; i < chat.size(); i++) {
            if (i != 0) {
                while (Long.parseLong(chat.get(i).getDate()) < Long.parseLong(chat.get(i - 1).getDate())) {
                    Chat a = chat.get(i - 1);
                    Chat b = chat.get(i);
                    chat.remove(i - 1);
                    chat.remove(i - 1);
                    chat.add(i - 1, b);
                    chat.add(i, a);
                    if (i != 1) {
                        i--;
                    } else {
                        break;
                    }
                }
            }
        }
        return chat;
    }

}
