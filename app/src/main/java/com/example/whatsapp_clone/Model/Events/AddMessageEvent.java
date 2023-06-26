package com.example.whatsapp_clone.Model.Events;

import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;

public class AddMessageEvent {
    private MessageEntity message;


    public AddMessageEvent(MessageEntity message) {
        this.message = message;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }
}
