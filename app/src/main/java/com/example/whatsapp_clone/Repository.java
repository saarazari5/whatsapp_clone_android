package com.example.whatsapp_clone;

import com.example.whatsapp_clone.Model.Retrofit.HTTPClientDataSource;


/**
 * Repository singleton , all Communication with ROOM and RETROFIT should be from this class
 * everything else will not be allowed!!!!
 * also you should work with the same classes for both services tou can either
 * 1) create an interface for DataSource and make HTTPClient and RoomClient , implement both ,
 * the repo can use strategy pattern to switch between them
 * 2) make sure all the data classes are support both Room and Retro aka USER,MESSAGE,CHAT
 *
 */
public class Repository {
    private static Repository instance;

    private HTTPClientDataSource httpClientDataSource;
    // Private constructor to prevent instantiation from other classes
    private Repository() {
        httpClientDataSource = new HTTPClientDataSource();
    }

    // Static method to get the instance
    public static Repository getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }

    // Other methods and variables...
}
