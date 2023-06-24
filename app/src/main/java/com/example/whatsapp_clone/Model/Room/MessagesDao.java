package com.example.whatsapp_clone.Model.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface MessagesDao {

    @Query("SELECT * FROM message WHERE chatId = :chatId")
    public LiveData<List<MessageEntity>> findMessages(int chatId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(MessageEntity ... message);

    @Query("DELETE FROM message WHERE chatId = :chatId")
    public void delete(Integer chatId);
}
