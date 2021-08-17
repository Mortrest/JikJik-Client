package client.shared;

import java.util.LinkedList;

public class MakeTweetResponse {
//            Tweets.makeTweet(overlayText.getText(),Tweets.getComment(),Users.getCurrentUser().getUsername(),Users.getCurrentUser().getFollowers());
    private final String text;
    private final String comment;
    private final String owner;
    private final LinkedList<String> followers;
    private final String imageUrl;
    private final boolean isImage;


    public MakeTweetResponse(String text, String comment, String owner, LinkedList<String> followers, String imageUrl, boolean isImage) {
        this.text = text;
        this.comment = comment;
        this.owner = owner;
        this.followers = followers;
        this.imageUrl = imageUrl;
        this.isImage = isImage;
    }

    public String getText() {
        return text;
    }

    public String getComment() {
        return comment;
    }

    public String getOwner() {
        return owner;
    }

    public LinkedList<String> getFollowers() {
        return followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isImage() {
        return isImage;
    }
}
