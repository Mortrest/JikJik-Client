package client.Models;

import java.util.LinkedList;

public class Room {
    private final String roomID;
    private final String date;
    private final String owner1;
    private final String owner2;
    private final LinkedList<String> members;
    private final String type;
    private final String groupName;
    protected int unread1;
    protected int unread2;

    public Room(String roomID, String date, String owner1,String owner2,int unread1,int unread2) {
        this.roomID = roomID;
        this.date = date;
        this.owner1 = owner1;
        this.owner2 = owner2;
        this.unread1 = unread1;
        this.unread2 = unread2;
        this.members = null;
        this.type = "pv";
        this.groupName = null;
    }
    public Room(String roomID,String date,LinkedList<String> members,String groupName){
        this.roomID = roomID;
        this.date = date;
        this.members = members;
        this.owner1 = null;
        this.owner2 = null;
        this.groupName = groupName;
        this.type = "gp";
    }

    public String getRoomID() {
        return roomID;
    }

    public String getOwner1() {
        return owner1;
    }

    public int getUnread1() {
        return unread1;
    }

    public LinkedList<String> getMembers() {
        return members;
    }

    public void setUnread1(int unread1) {
        this.unread1 = unread1;
    }

    public int getUnread2() {
        return unread2;
    }

    public void setUnread2(int unread2) {
        this.unread2 = unread2;
    }

    public String getOwner2() {
        return owner2;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getGroupName() {
        return groupName;
    }
}
