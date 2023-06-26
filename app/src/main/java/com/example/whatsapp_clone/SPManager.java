package com.example.whatsapp_clone;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.whatsapp_clone.Model.User;
import com.google.gson.Gson;

public class SPManager {
    private final SharedPreferences sharedPreferences;
    public static final String KEY_PREFERENCE_NAME = "SPManager";

    public SPManager(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }


    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putUser(User.UserRegistration user, String key) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

    public User.UserRegistration getUser(String key) {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, User.UserRegistration.class);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}