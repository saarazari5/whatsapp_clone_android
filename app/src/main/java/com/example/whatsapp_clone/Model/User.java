package com.example.whatsapp_clone.Model;

import com.example.whatsapp_clone.Model.Utils.Utils;

public class User {
    public String username;
    public String displayName;
    public String profilePic;

    public User(String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public static class Mock extends User {
        public Mock() {
            super("saarazari5@gmail.com",
                    "Saar Azari",
                    Utils.mockImage);
        }
    }

}
