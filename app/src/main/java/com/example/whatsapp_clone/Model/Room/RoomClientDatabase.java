package com.example.whatsapp_clone.Model.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.MessageEntity;

@Database(entities = {Chat.class, MessageEntity.class}, version = 8)
public abstract class RoomClientDatabase extends RoomDatabase {
    public abstract ChatsDao chatsDao();
    public abstract MessagesDao messagesDao();
}
