package com.example.whatsapp_clone.Model.Room.Converters;

import androidx.room.TypeConverter;

import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MessageListConverter {

    @TypeConverter
    public static List<Message> fromJson(String json) {
        return new Gson().fromJson(json, new TypeToken<List<Message>>() {}.getType());
    }

    @TypeConverter
    public static String toJson(List<Message> messageList) {
        return new Gson().toJson(messageList);
    }
}
