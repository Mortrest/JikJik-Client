package client.Models;

import java.time.LocalDateTime;

public class Chat {
    private final String ID;
    private final String roomID;
    private String text;
    private LocalDateTime date;
    private final String owner;
    private final String image;
    private boolean edited;
    private boolean forwarded;
    private boolean isScheduled;
    private int type;

    public Chat(String ID,String roomID, String text, LocalDateTime date, String owner,boolean edited,boolean forwarded, int type,boolean isScheduled) {
        this.ID = ID;
        this.roomID = roomID;
        this.text = text;
        this.date = date;
        this.owner = owner;
        this.edited = edited;
        this.forwarded = forwarded;
        this.image = null;
        this.date = date;
        this.type = type;
        this.isScheduled = isScheduled;
    }

    public Chat(String ID, String roomID, String text, LocalDateTime date, String owner, boolean edited, boolean forwarded, String image, int type,boolean isScheduled){
        this.ID = ID;
        this.roomID = roomID;
        this.text = text;
        this.date = date;
        this.owner = owner;
        this.edited = edited;
        this.forwarded = forwarded;
        this.image = image;
        this.date = date;
        this.type = type;
        this.isScheduled = isScheduled;
    }
    public String getID(){
        return ID;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isForwarded() {
        return forwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.forwarded = forwarded;
    }

    public String getText() {
        return text;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getOwner() {
        return owner;
    }


    public LocalDateTime getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }
}
