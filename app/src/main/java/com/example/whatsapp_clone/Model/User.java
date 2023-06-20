package com.example.whatsapp_clone.Model;

import com.example.whatsapp_clone.Model.Utils.Utils;

public class User {

    public String username;
    public String displayName;
    public String profilePic;

    public String password;

    public User(String username, String displayName, String password,String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.password = password;
        this.profilePic = profilePic;
    }

    public static class Mock extends User {
        public Mock() {
            super("saarazari5@gmail.com",
                    "Saar Azari",
                    "123123123",
                    Utils.mockImage);
        }
    }

}
