package com.example.whatsapp_clone.Model;
import androidx.room.Embedded;

public class Message {
    public int messageId;

    @Embedded
    public User sender;
    public String content;
    public String created;


    public Message(int messageId, User sender, String content, String created) {
        this.messageId = messageId;
        this.sender = sender;
        this.content = content;
        this.created = created;
    }

    public static class Mock extends  Message {
        public Mock() {
            super(1,
                    new User.Mock(),
                    "Lorem Ipsum, Ipsum Lorem !!! ",
                    "12/06/2023" );
        }
    }

    public static class Mock2 extends  Message {
        public Mock2() {
            super(2,
                    new User.Mock2(),
                    "Lorem Ipsum, Ipsum Lorem !!! ",
                    "12/06/2023" );
        }
    }
}
