package com.example.whatsapp_clone.Model.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Message;

import java.util.List;

public class RoomClientDataSource {

    private RoomClientDatabase roomClientDataSource;

    public RoomClientDataSource(final Context context) {
        roomClientDataSource = Room
                .databaseBuilder(context, RoomClientDatabase.class, "app-db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void insert(Chat... chats) {
        new insertChatAsyncTask(roomClientDataSource.chatsDao())
                .execute(chats);
    }

    public void insert(Message ... messages) {
        new insertMessageAsyncTask(roomClientDataSource.messagesDao())
                .execute(messages);
    }

    public LiveData<List<Message>>findMessages(String sender) {
        return roomClientDataSource.messagesDao().findMessages(sender);
    }

    public LiveData<List<Chat>>findChats() {
        return roomClientDataSource.chatsDao().getAll();
    }


    private static class insertChatAsyncTask extends AsyncTask<Chat, Void, Void> {
        private ChatsDao mChatAsyncTaskDao;

        insertChatAsyncTask(ChatsDao dao) {
            mChatAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Chat... chats) {
            mChatAsyncTaskDao.insert(chats);
            return null;
        }
    }

    private static class insertMessageAsyncTask extends AsyncTask<Message, Void , Void> {

        private MessagesDao mMessageAsyncTaskDao;



         insertMessageAsyncTask(MessagesDao dao) {
            mMessageAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Message... messages) {
             mMessageAsyncTaskDao.insert(messages);
            return null;
        }
    }
}
