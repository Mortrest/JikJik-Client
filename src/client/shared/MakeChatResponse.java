package client.shared;

public class MakeChatResponse {
    String text;
    String owner;
    String roomID;
    String imageUrl;
    boolean isImage;
    boolean sch;

    public MakeChatResponse(String text, String owner, String roomID, String imageUrl, boolean isImage, boolean sch) {
        this.text = text;
        this.owner = owner;
        this.roomID = roomID;
        this.imageUrl = imageUrl;
        this.isImage = isImage;
        this.sch = sch;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public boolean isSch() {
        return sch;
    }

    public void setSch(boolean sch) {
        this.sch = sch;
    }
}