package client.shared;

import client.Models.Chat;
import client.Models.Room;

import java.util.LinkedList;

public class RoomStuff {

    LinkedList<Chat> chats;
    Room room;

    public RoomStuff(LinkedList<Chat> chats, Room room) {
        this.chats = chats;
        this.room = room;
    }

    public LinkedList<Chat> getChats() {
        return chats;
    }

    public void setChats(LinkedList<Chat> chats) {
        this.chats = chats;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
