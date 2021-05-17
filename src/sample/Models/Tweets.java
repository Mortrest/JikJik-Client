package sample.Models;

import sample.ModelLoader;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;


public class Tweets {
    public LinkedList<Tweet> getTweets() {
        return tweets;
    }
    static String comment;
    static LinkedList<Tweet> tweets;
    static ModelLoader ml;
    static String image;
    static String tweetID;
    static String forwardTweetID;
    public Tweets(ModelLoader modelLoader) {
        tweets = modelLoader.loadTweets();
        this.ml = modelLoader;
    }

    public static String getImage() {
        return image;
    }

    public static void setImage(String image) {
        Tweets.image = image;
    }

    public static String getForwardTweetID() {
        return forwardTweetID;
    }

    public static void setForwardTweetID(String forwardTweetID) {
        Tweets.forwardTweetID = forwardTweetID;
    }

    public static String getComment() {
        return comment;
    }

    public static void setComment(String comment) {
        Tweets.comment = comment;
    }

    public static String getTweetID() {
        return tweetID;
    }

    public static void setTweetID(String tweetID) {
        Tweets.tweetID = tweetID;
    }

    public static void reportUser(User user, String tweetID){
        Tweet tweet = Tweets.search(tweetID);
        int rep = tweet.getReported();
        tweet.setReported(rep+1);
        ml.save(tweets,"Tweets");
        ml.log(tweetID + " Reported");
    }

    public LinkedList<Tweet> getComments(String tweetID) {
        LinkedList<Tweet> comments = new LinkedList<>();
        for (Tweet tw : tweets){
            if (tw.getParent().equals(tweetID) && Users.searchUsername(tw.getOwner()).isActive()){
                comments.add(tw);
            }
        }
        System.out.println(comments + "fff");
        return comments;
    }

    // Searching by ID
    public static Tweet search(String ID) {
        for (Tweet tw : tweets) {
            if (tw.getID().equals(ID)) {
                return tw;
            }
        }
        return null;
    }

    public static void makeTweetImage(String text,String image, String parent, String owner, LinkedList<String> followers) {
        Date date = new Date();
        LinkedList<String> str2 = new LinkedList<>();
        Random random = new Random();
        Tweet tweet = new Tweet(Integer.toString(random.nextInt(100000)), text,parent, followers, Long.toString(date.getTime()), str2, owner, false,image,0);
        tweets.add(tweet);
        ml.log("Tweets-"+"Tweet Created " + text);
        ml.save(tweets,"Tweets");
    }

    // Making Tweets
    public static void makeTweet(String text, String parent, String owner, LinkedList<String> followers) {
        Date date = new Date();
        LinkedList<String> str2 = new LinkedList<>();
        Random random = new Random();
        Tweet tweet = new Tweet(Integer.toString(random.nextInt(100000)), text,parent, followers, Long.toString(date.getTime()), str2, owner, false,0);
        tweets.add(tweet);
        ml.log("Tweets-"+"Tweet Created " + text);
        ml.save(tweets,"Tweets");
    }

    // ُShowing Tweets
    public LinkedList<Tweet> showTweetOwnPage(Class<Users> users, String username, int type) {
        LinkedList<Tweet> tw = new LinkedList<>();
        if (type == 3) {
            for (Tweet t : tweets) {
                if (Users.searchUsername(t.owner).isActive) {
                    if (!Users.searchUsername(username).getMuted().contains(t.owner) && t.getParent().equals("0")) {
                        if (Users.searchUsername(t.owner).isPrivate()) {
                            if (Users.searchUsername(username).getFollowing().contains(t.owner)) {
                                tw.add(t);
                            }
                        } else {
                            tw.add(t);
                        }
                    }
                }
            }
            tw = sortByLike(tw);
        }
        else {
            User user = Users.searchUsername(username);
            for (Tweet t : tweets) {
                User target = Users.searchUsername(t.getOwner());
                assert target != null;
                if (target.isActive && t.getParent().equals("0") && !user.getMuted().contains(target.getUsername())) {
                    if (t.owner.equals(username)) {
                        tw.add(t);
                    }
                    if (type == 2) {
                        if (t.getUsers() != null) {
                            for (String str : t.users) {
                                if (str.equals(username) && t.getParent().equals("0")) {
                                    if (!tw.contains(t)) {
                                        tw.add(t);
                                    }
                                }
                            }
                        }
                    }
                }
                if (type == 1 || type == 2) {
                    tw = sortByDate(tw);
                }
            }
        }
        return tw;

    }

    // Liking Tweets
    public static void likeTweet(User user, Tweet tweet) {
        if (tweet.getLikes().contains(user.getUsername())) {
            tweet.getLikes().remove(user.getUsername());
            ml.log("Tweets-"+user.getUsername() + " Liked Tweet ID " + tweet.getID());
        } else {
            tweet.getLikes().add(user.getUsername());
            ml.log("Tweets-"+user.getUsername() + " Unliked Tweet ID " + tweet.getID());
        }
        ml.save(tweets,"Tweets");
    }

    // Retweeting
    public static void reTweet(Tweet tweet, User user) {
        Date date = new Date();
        LinkedList<String> str2 = new LinkedList<>();
        Tweet tw;
        if (tweet.getImage() == null) {
            tw = new Tweet(Integer.toString(tweets.size() + 1), tweet.getText(), tweet.getParent(), user.getFollowers(), Long.toString(date.getTime()), str2, user.getUsername(), true, 0);
        } else {
            tw = new Tweet(Integer.toString(tweets.size() + 1), tweet.getText(), tweet.getParent(), user.getFollowers(), Long.toString(date.getTime()), str2, user.getUsername(), true,tweet.getImage(), 0);
        }
        tweets.add(tw);
        ml.log("Tweets-"+user.getUsername() + " Retweeted Tweet ID " + tweet.getID());
        ml.save(tweets,"Tweets");
    }

    // Adding tweets to newly followed
    public void follow(String current, String target) {
        for (Tweet tw : tweets) {
            if (tw.owner.equals(target)) {
                tw.users.add(current);
            }
        }
        ml.log("Tweets-"+current + " Followed " + target);
        ml.save(tweets,"Tweets");
    }

    // Removing dependents with unfollow
    public void unfollow(String current, String target) {
        for (Tweet tw : tweets) {
            if (tw.owner.equals(target)) {
                tw.users.remove(current);
            }
        }
        ml.log("Tweets-"+current + " Unfollowed " + target);
        ml.save(tweets,"Tweets");
    }

    // Sorting by date
    public LinkedList<Tweet> sortByDate(LinkedList<Tweet> tweet) {
        for (int i = 0; i < tweet.size(); i++) {
            if (i != 0) {
                while (Long.parseLong(tweet.get(i).getDate()) > Long.parseLong(tweet.get(i - 1).getDate())) {
                    Tweet a = tweet.get(i - 1);
                    Tweet b = tweet.get(i);
                    tweet.remove(i - 1);
                    tweet.remove(i - 1);
                    tweet.add(i - 1, b);
                    tweet.add(i, a);
                    if (i != 1) {
                        i--;
                    } else {
                        break;
                    }
                }
            }
        }
        return tweet;
    }

    // Sorting by likes
    public LinkedList<Tweet> sortByLike(LinkedList<Tweet> tweet) {
        for (int i = 0; i < tweet.size(); i++) {
            if (i != 0) {
                while (tweet.get(i).getLikes().size() > (tweet.get(i - 1).getLikes().size())) {
                    Tweet a = tweet.get(i - 1);
                    Tweet b = tweet.get(i);
                    tweet.remove(i - 1);
                    tweet.remove(i - 1);
                    tweet.add(i - 1, b);
                    tweet.add(i, a);
                    if (i != 1) {
                        i--;
                    } else {
                        break;
                    }
                }
            }
        }
        return tweet;

    }

    public void deleteProfile(User user) {
        tweets.removeIf(tw -> tw.owner.equals(user.getUsername()));
        for(Tweet tw : tweets){
            tw.getLikes().removeIf(str -> str.equals(user.getUsername()));
        }
        ml.log("Tweets-"+user.getUsername() + " Tweets Deleted!");
        ml.save(tweets,"Tweets");

    }

}
