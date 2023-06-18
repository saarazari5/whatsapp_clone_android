package com.example.whatsapp_clone.Model.Room.Converters;

import androidx.room.TypeConverter;

import com.example.whatsapp_clone.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class UserListConverter {
    @TypeConverter
    public static List<User> fromJson(String json) {
        return new Gson().fromJson(json, new TypeToken<List<User>>() {}.getType());
    }

    @TypeConverter
    public static String toJson(List<User> userList) {
        return new Gson().toJson(userList);
    }
}

