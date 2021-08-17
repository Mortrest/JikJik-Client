package client.shared;

import java.util.LinkedList;

public class UpdateCategoriesResponse {
    String str;
    LinkedList<String> followers;

    public UpdateCategoriesResponse(String str, LinkedList<String> followers) {
        this.str = str;
        this.followers = followers;
    }

    public String getStr() {
        return str;
    }

    public LinkedList<String> getFollowers() {
        return followers;
    }
}
