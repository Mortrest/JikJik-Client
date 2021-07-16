package client.shared;

import client.Models.Chat;

import java.util.LinkedList;

public class ShowChatResponse {
    LinkedList<Chat> chats;

    public ShowChatResponse(LinkedList<Chat> chats){
        this.chats = chats;
    }

    public LinkedList<Chat> getChats() {
        return chats;
    }

}
