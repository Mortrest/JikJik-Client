package client.shared;

import client.Models.Tweet;

import java.util.LinkedList;

public class GetTweetsResponse {

    LinkedList<Tweet> tweets;

    public GetTweetsResponse(LinkedList<Tweet> tweets) {
        this.tweets = tweets;
    }

    public LinkedList<Tweet> getTweets() {
        return tweets;
    }
}
