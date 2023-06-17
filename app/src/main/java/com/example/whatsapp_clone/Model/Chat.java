package com.example.whatsapp_clone.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Chat implements Serializable {

    @SerializedName("id")
    public int chatId;
    public Message lastMessage;
    public ArrayList<User>users;

    public Chat(int chatId, Message lastMessage, ArrayList<User> users) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.users = users;
    }

    public static class Mock extends Chat {
        public Mock() {
            super(1, new Message.Mock(), new ArrayList<>());
            for (int i = 0; i <=1 ; i ++) {
                this.users.add(new User.Mock());
            }
        }
    }
}
