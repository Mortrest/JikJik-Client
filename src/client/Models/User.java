package client.Models;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class User {
    private final String ID;
    protected String firstName;protected String lastName;
    private final String username;
    protected String password;
    protected String email;
    protected String phoneNumber;
    protected String birthdate;
    protected String info;
    protected LocalDateTime lastSeen;
    public String getID() {
        return ID;
    }
    @Override
    public String toString() {
        return "User{" +
                "ID='" + ID + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
    protected LinkedList<String> followers;
    protected LinkedList<String> following;
    protected LinkedList<String> blackList;
    protected LinkedList<String> muted;
    protected LinkedList<LinkedList<String>> categories;
    protected boolean isPrivate;
    protected boolean isLastSeenAvailable;
    boolean isActive;
    protected String profilePic;
    public String getPassword() {
        return password;
    }

    public User(String ID, String firstName, String lastName, String username, String password, String info, LocalDateTime lastSeen, LinkedList<String> followers,LinkedList<String>following,LinkedList<String> blackList,LinkedList<String> muted, LinkedList<LinkedList<String>> categories,boolean isLastSeenAvailable, boolean isPrivate, boolean isActive, String phoneNumber,String email,String birthdate
        ,String profilePic) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.info = info;
        this.lastSeen = lastSeen;
        this.followers = followers;
        this.following = following;
        this.blackList = blackList;
        this.categories = categories;
        this.muted = muted;
        this.isPrivate = isPrivate;
        this.isActive = isActive;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.isLastSeenAvailable = isLastSeenAvailable;
        this.profilePic = profilePic;
    }


    public User(String ID, String firstName, String lastName, String username, String password, String info, String phoneNumber,String email,String birthdate,String profilePic) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.info = info;
        this.following = new LinkedList<>();
        this.followers = new LinkedList<>();
        this.blackList = new LinkedList<>();
        this.categories = new LinkedList<>();
        this.muted = new LinkedList<>();
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
        this.profilePic = profilePic;
        isPrivate = false;
        isActive = true;
        lastSeen = LocalDateTime.now();
        this.isLastSeenAvailable = true;
    }
    public String getUsername() {
        return username;
    }

    public String getName(){
        return firstName + " " + lastName;
    }
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LinkedList<String> getMuted() {
        return muted;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isActive() {
        return isActive;
    }

    public LinkedList<LinkedList<String>> getCategories() {
        return categories;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isLastSeenAvailable() {
        return isLastSeenAvailable;
    }

    public void setLastSeenAvailable(boolean lastSeenAvailable) {
        isLastSeenAvailable = lastSeenAvailable;
    }

    public String getLastName() {
        return lastName;
    }

    public String getInfo() {
        return info;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime localDateTime) {
        this.lastSeen = localDateTime;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void addCatg(LinkedList<String> cat){
        categories.add(cat);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LinkedList<String> getFollowers() {
        return followers;
    }



    public LinkedList<String> getFollowing() {
        return following;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public LinkedList<String> getBlackList() {
        return blackList;
    }



}
