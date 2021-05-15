package sample.Models;

import sample.ModelLoader;

import java.util.LinkedList;
import java.util.Random;

public class Users {
    static Tweets tweets;
    static LinkedList<User> users;
    static Chats chats;
    static Notifs notifs;

    static User Profile;
    static User currentUser;
    static ModelLoader ml;

    // Constructor
    public Users(ModelLoader modelLoader, Tweets tweets,Chats chats,Notifs notifs){
        users = modelLoader.loadUsers();
        ml = modelLoader;
        Users.tweets = tweets;
        Users.chats = chats;
        Users.notifs = notifs;
    }

    public static User getProfile() {
        return Profile;
    }

    public static void setProfile(User profile) {
        Profile = profile;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Users.currentUser = currentUser;
    }


    public static Chats getChats(){
        return chats;
    }

    public static Tweets getTweets() {
        return tweets;
    }

    public static LinkedList<User> getUsers() {
        return users;
    }

    public static void log(String msg){
        ml.log(msg);
    }

    public static Notifs getNotifs() {
        return notifs;
    }

    public static void save(){
        ml.save(users,"Users");
    }

    public static void createCatg(User user,LinkedList<String> cat){
        user.addCatg(cat);
        save();
    }

    // Username reservation for sign up
    public static boolean isViable(String username,String email){
        for (User us:users){
                if (username.equals(us.getUsername()) || email.equals(us.getEmail())) {
                    return false;
                }
        }
        return true;
    }

    public static void updateCategories(LinkedList<String> followers,String str){
        LinkedList<LinkedList<String>> catg = Users.getCurrentUser().getCategories();
        int index = catg.indexOf(followers);
        int index2 = catg.get(index).indexOf(str);
        System.out.println(catg.get(index));
        catg.get(index).remove(index2);
        System.out.println(catg.get(index));
        ml.save(users,"Users");
    }

    // Sign up user
    public static void signUp(String username, String fName,String lName, String email, String password){
        Random random = new Random();
        User user = new User(Integer.toString(random.nextInt(10000)),fName,lName,username,password,"","",email,"",null);
        users.add(user);
        Chats.createSavedMsg(user.getUsername());
        ml.save(users,"Users");
    }

    // Following profiles
    public static void followProfile(User user,String target){
        if (!user.getFollowing().contains(target)) {
            user.getFollowing().add(target);
            searchUsername(target).getFollowers().add(user.getUsername());
            tweets.follow(user.getUsername(), target);
            notifs.makeNotif((user.getUsername() + " Started following you!"), target, "1");
            ml.save(users,"Users");
        } else {
            user.getFollowing().remove(target);
            tweets.unfollow(user.getUsername(),target);
            notifs.makeNotif((user.getUsername() + " Stopped following you!"),target,"1");

            ml.save(users,"Users");

        }
    }


    // Blocking profiles
    public static void blockProfile(User user, String target){
        if (!user.getMuted().contains(target)){
            user.getMuted().add(target);
        }
        if (user.getBlackList().contains(target)){
            user.getBlackList().remove(target);
            user.getMuted().remove(target);
        } else {
            user.getBlackList().add(target);
            user.getFollowing().removeIf(str -> str.equals(target));
            user.getFollowers().removeIf(str -> str.equals(target));
        }

        ml.save(users,"Users");
    }

    public static void muteProfile(User user, String target){
        if (user.getMuted().contains(target)){
            user.getMuted().remove(target);
            ml.log("Users-"+user.getUsername() + " Unmuted " + target );
        } else {
            user.getMuted().add(target);
            ml.log("Users-"+user.getUsername() + " Muted " + target );
        }
        ml.save(users,"Users");
    }

    // Sign in user
    public static boolean signIn(String username,String password){
        if (users.size() == 0){
            System.out.println("null darim");
        }
        for (User us : users){
            if (us.getUsername().equals(username) && us.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public void saveCatg(User user,LinkedList<String> catg){
        for (LinkedList<String> cat : user.getCategories()){
            if (cat.get(0).equals(catg.get(0))){
                user.getCategories().remove(cat);
                user.getCategories().add(catg);
                break;
            }
        }
        ml.save(users,"Users");
    }

    public static void createCatg(User user, String name){
        LinkedList<String> n = new LinkedList<>();
        n.add(name);
        user.getCategories().add(n);
        ml.log("Users-"+"Category " + name + " Created");
        ml.save(users,"Users");
    }

    // Searching with ID
    public static User searchUsername(String username) throws NullPointerException{
        for (User us : users){
            if (us.getUsername().equals(username)){
                return us;
            }
        }
        return null;
    }

    public static void deleteProfile(User user){
        for (String us : user.getFollowers()){
            searchUsername(us).getFollowing().remove(user.getUsername());
        }
        for (String us : user.getFollowing()) {
            searchUsername(us).getFollowers().remove(user.getUsername());
        }
        for (LinkedList<String> us : user.getCategories()){
            us.remove(user.getUsername());
        }
        users.remove(user);
        ml.save(users,"Users");
        tweets.deleteProfile(user);
        Chats.rooms.removeIf(room -> room.getOwner2().equals(user.getUsername()) || room.getOwner1().equals(user.getUsername()));
        ml.save(Chats.chats,"Chats");
        notifs.notifs.removeIf(notif -> notif.getOwner().equals(user.getUsername()));
        ml.save(notifs.notifs,"Notifs");
        ml.log("Users-"+user.getUsername() + " Profile deleted");
    }





    public static void deactivate(User user){
        user.setActive(false);
        ml.save(users,"Users");
    }
    // Making tweet
    public static void makeTweet(User userf,String textf){
        Tweets.makeTweet(textf,"0",userf.getUsername(),userf.getFollowers());
    }


}
