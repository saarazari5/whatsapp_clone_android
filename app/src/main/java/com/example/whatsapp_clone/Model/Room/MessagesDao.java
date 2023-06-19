package com.example.whatsapp_clone.Model.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.whatsapp_clone.Model.Message;

import java.util.List;

@Dao
public interface MessagesDao {

    @Query("SELECT * FROM message WHERE username = :sender")
    public LiveData<List<Message>> findMessages(String sender);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Message ... message);
}
