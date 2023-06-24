package com.example.whatsapp_clone.Model.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;

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

    public void insert(MessageEntity... messages) {
        new insertMessageAsyncTask(roomClientDataSource.messagesDao())
                .execute(messages);
    }

    public void deleteChat(Integer... chatIds) {
        new deleteChatAsyncTask(roomClientDataSource.chatsDao(), roomClientDataSource.messagesDao())
                .execute(chatIds);
    }

    public void deleteAll() {
        new deleteAllAsyncTask(roomClientDataSource).execute();
    }

    public LiveData<List<MessageEntity>> findMessages(int chatId) {
        return roomClientDataSource.messagesDao().findMessages(chatId);
    }

    public LiveData<List<Chat>> findChats() {
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

    // delete chat stuff:
    private static class deleteChatAsyncTask extends AsyncTask<Integer, Void, Void> {

        private ChatsDao mChatAsyncTaskDao;
        private MessagesDao mMessagesDao;

        deleteChatAsyncTask(ChatsDao chatsDao, MessagesDao messagesDao) {
            this.mChatAsyncTaskDao = chatsDao;
            this.mMessagesDao = messagesDao;
        }

        @Override
        protected Void doInBackground(Integer... chatIds) {
            for (Integer chatId : chatIds) {
                mMessagesDao.delete(chatId);
                mChatAsyncTaskDao.delete(chatId);
            }
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private RoomClientDatabase roomClientDataSource;


        deleteAllAsyncTask(RoomClientDatabase roomClientDataSource) {
            this.roomClientDataSource = roomClientDataSource;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            roomClientDataSource.clearAllTables();
            return null;
        }
    }


    private static class insertMessageAsyncTask extends AsyncTask<MessageEntity, Void, Void> {

        private MessagesDao mMessageAsyncTaskDao;


        insertMessageAsyncTask(MessagesDao dao) {
            mMessageAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(MessageEntity... messages) {
            mMessageAsyncTaskDao.insert(messages);
            return null;
        }
    }
}
