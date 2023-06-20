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


    /**
     * use instance of this subclass of User when creating a new user with password,
     * the separation is needed since only in register case this class need a password,
     * other responses that return a user from server does not need a password
     */
    public static class UserRegistration extends User {

        String password;
        public UserRegistration(String username,
                                String password ,
                                String displayName,
                                String profilePic) {
            super(username, displayName, profilePic);
            this.password = password;
        }
    }

    public static class Mock extends User {
        public Mock() {
            super("ssaar@gmail.com",
                    "Saar Azari",
                    "123123123",
                    Utils.mockImage);
        }
    }
}


