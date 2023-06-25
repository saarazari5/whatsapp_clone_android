package com.example.whatsapp_clone.Model;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.whatsapp_clone.Model.Room.Converters.MessageListConverter;
import com.example.whatsapp_clone.Model.Room.Converters.UserListConverter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "chats")
@TypeConverters({UserListConverter.class, MessageListConverter.class})

public class Chat implements Serializable {

    @SerializedName("id")
    @PrimaryKey
    public int chatId;

    @Embedded
    public Message lastMessage;
    public List<User> users;

    @Ignore
    @SerializedName("user")
    public User addedUser;

    public transient List<Message> messages;

    public Chat(int chatId, Message lastMessage, List<User> users) {
        this.chatId = chatId;
        this.lastMessage = lastMessage;
        this.users = users;
        this.messages = new ArrayList<>();
    }

    public static class Mock extends Chat {
        public Mock() {
            super(1, new Message.Mock(), new ArrayList<>());
            for (int i = 0; i <=1 ; i ++) {
                this.users.add(new User.Mock());
            }

            for (int i = 0 ; i < 10; i++) {
                this.messages.add(new Message.Mock());
            }


        }
    }
}
