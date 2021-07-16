package client.shared;

import client.Models.Notif;

import java.util.LinkedList;

public class NotifResponse {
    LinkedList<Notif> notifs;

    public NotifResponse(LinkedList<Notif> notifs){
        this.notifs = notifs;
    }

    public LinkedList<Notif> getNotifs() {
        return notifs;
    }
}
