package sample.Models;

import sample.ModelLoader;

import java.util.Date;
import java.util.LinkedList;

// Owner kasie ke req dade va owner 2 kasie ke mafoole
public class Notifs {
    static LinkedList<Notif> notifs;
    static ModelLoader ml;

    public Notifs(ModelLoader ml){
        this.ml = ml;
        this.notifs = ml.loadNotifs();
    }

    // Making notifs
    public static void makeNotif(String text, String owner, String type){
        Date date = new Date();
        Notif notif = new Notif(owner,text,Long.toString(date.getTime()),false,type);
        notifs.add(notif);
        ml.log("Notifs-"+"Notification " + text +" Created");
        ml.save(notifs,"Notifs");
    }

    public static void acceptOrDeclineReq(Notif notif,int isAccepted){
        User owner =  Users.searchUsername(notif.getOwner());
        User owner2 = Users.searchUsername(notif.getOwner2());
        notifs.remove(notif);
        String str = "";
        if (isAccepted == 1) {
            owner.getFollowing().add(owner2.getUsername());
            owner2.getFollowers().add(owner.getUsername());
            str = "Accepted";
            makeNotif(owner.getUsername() + " Has " + str + " Your Request", owner2.getUsername(), "1");
        } else if (isAccepted == 2) {
            str = "Declined";
            makeNotif(owner.getUsername() + " Has " + str + "Your Request", owner2.getUsername(), "1");
        }

        ml.save(Users.users,"Users");
    }

    public void deleteNotif(Notif notif){
        notifs.remove(notif);
        ml.log("Notifs-"+"Notification " + notif.getText() +" Deleted");
        ml.save(notifs,"Notifs");
    }

    public static void makeRequest(String owner,String type,String owner2){
        String text = owner2 + " Has Requested to Follow";
        Date date = new Date();
        Notif notif = new Notif(owner,text,Long.toString(date.getTime()),false,type,owner2);
        notifs.add(notif);
        ml.log("Notifs-"+owner + " Requested to " + owner2);
        ml.save(notifs,"Notifs");
    }

    // Showing notifs
    public LinkedList<Notif> showNotif(String username){
        LinkedList<Notif> n = new LinkedList<>();
        for (Notif notif : notifs){
            if (notif.getOwner().equals(username)){
                n.add(notif);
            }
        }
        return n;
    }

    public LinkedList<Notif> showMyReqs(String username){
        LinkedList<Notif> n = new LinkedList<>();
        for (Notif notif : notifs){
            if (notif.getType().equals("2")) {
                if (notif.getOwner2().equals(username)) {
                    n.add(notif);
                }
            }
        }
        return n;
    }
}
