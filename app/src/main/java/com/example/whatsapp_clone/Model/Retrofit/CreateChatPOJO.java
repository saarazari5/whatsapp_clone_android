package com.example.whatsapp_clone.Model.Retrofit;

import com.example.whatsapp_clone.Model.User;
import com.google.gson.annotations.SerializedName;

public class CreateChatPOJO {
    public int id;
    @SerializedName("user")
    public User addedUser;
}
