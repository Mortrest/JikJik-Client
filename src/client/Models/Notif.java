package client.Models;

public class Notif {
    private final String owner;
    private final String owner2;
    private final String text;
    private final String date;
    private final String type;
    protected boolean isAccepted;
    private boolean isRead;

    public Notif(String owner, String text, String date, boolean isRead,String type) {
        this.owner = owner;
        this.owner2 = "";
        this.text = text;
        this.date = date;
        this.isRead = isRead;
        this.type = type;
    }

    public Notif(String owner, String text, String date, boolean isRead,String type,String owner2) {
        this.owner = owner;
        this.owner2 = owner2;
        this.text = text;
        this.date = date;
        this.isRead = isRead;
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwner2() {
        return owner2;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return isRead;
    }
}
