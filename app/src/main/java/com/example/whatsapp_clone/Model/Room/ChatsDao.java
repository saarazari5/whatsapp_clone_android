package com.example.whatsapp_clone.Model.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whatsapp_clone.Model.Chat;

import java.util.List;

@Dao
public interface ChatsDao {

    @Query("SELECT * FROM chats")
    public LiveData<List<Chat>> getAll();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Chat ... chats);

    @Update
    public void updateChat(Chat chat);

    @Query("DELETE FROM chats WHERE chatId = :chatId")
    public void delete(Integer chatId);

}
