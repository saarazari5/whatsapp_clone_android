package com.example.whatsapp_clone.Model;

public class Message {
    int messageId;
    public User sender;
    public String content;
    public String date;

    public Message(int messageId, User sender, String content, String date) {
        this.messageId = messageId;
        this.sender = sender;
        this.content = content;
        this.date = date;
    }

    public static class Mock extends  Message {
        public Mock() {
            super(1,
                    new User.Mock(),
                    "Lorem Ipsum, Ipsum Lorem !!! ",
                    "12/06/2023" );
        }
    }
}
