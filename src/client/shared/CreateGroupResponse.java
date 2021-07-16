package client.shared;

import java.util.LinkedList;

public class CreateGroupResponse {
    LinkedList<String> members;
    String groupName;

    public CreateGroupResponse(LinkedList<String> members, String groupName){
        this.groupName = groupName;
        this.members = members;
    }

    public LinkedList<String> getMembers() {
        return members;
    }

    public String getGroupName() {
        return groupName;
    }
}
