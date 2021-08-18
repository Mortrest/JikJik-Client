package client.utils;

public class OffineChat {
    String text;
    String imageStr;
    String roomID;

    public OffineChat(String text, String imageStr, String roomID) {
        this.text = text;
        this.imageStr = imageStr;
        this.roomID = roomID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
