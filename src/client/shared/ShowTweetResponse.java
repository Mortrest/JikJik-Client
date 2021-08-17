package client.shared;

import client.Models.Tweet;

import java.util.LinkedList;

public class ShowTweetResponse {
    LinkedList<Tweet> homeTweets;
    LinkedList<Tweet> exploreTweets;
    LinkedList<Tweet> profileTweets;

    public ShowTweetResponse(LinkedList<Tweet> homeTweets, LinkedList<Tweet> exploreTweets, LinkedList<Tweet> profileTweets) {
        this.homeTweets = homeTweets;
        this.exploreTweets = exploreTweets;
        this.profileTweets = profileTweets;
    }

    public LinkedList<Tweet> getHomeTweets() {
        return homeTweets;
    }

    public void setHomeTweets(LinkedList<Tweet> homeTweets) {
        this.homeTweets = homeTweets;
    }

    public LinkedList<Tweet> getExploreTweets() {
        return exploreTweets;
    }

    public void setExploreTweets(LinkedList<Tweet> exploreTweets) {
        this.exploreTweets = exploreTweets;
    }

    public LinkedList<Tweet> getProfileTweets() {
        return profileTweets;
    }

    public void setProfileTweets(LinkedList<Tweet> profileTweets) {
        this.profileTweets = profileTweets;
    }
}
