package com.example.whatsapp_clone.Model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// needed to create a new entity for message room since we will need to add chatID variable to the query
@Entity(tableName = "message")
public class MessageEntity {
    @PrimaryKey
    public int messageId;
    @Embedded
    public User sender;
    public String content;
    public String created;
    public int chatId;

    public MessageEntity(int messageId, User sender, String content, String created, int chatId) {
        this.messageId = messageId;
        this.sender = sender;
        this.content = content;
        this.created = created;
        this.chatId = chatId;
    }


}
