package com.example.whatsapp_clone.Model.Utils;

import com.example.whatsapp_clone.Model.Chat;

import java.util.ArrayList;
import java.util.List;

public class Mocks {

    public static class ChatsMocks {
        public static List<Chat.Mock> getMockChats() {
            ArrayList<Chat.Mock> mock = new ArrayList<>();
            mock.add(new Chat.Mock());

            return mock;
        }
    }
}
