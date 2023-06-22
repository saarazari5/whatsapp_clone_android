package com.example.whatsapp_clone.Model.Adapters;

import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;

import java.util.ArrayList;
import java.util.List;

public class MessageToMessageEntityAdapter {
   public List<MessageEntity> adapt(List<Message> messages, int chatId) {
        List<MessageEntity> messageEntities = new ArrayList<>();
        messages.forEach(message -> messageEntities.add(adapt(message,chatId)));
        return messageEntities;
    }


    public MessageEntity adapt(Message message, int chatId) {
        return new MessageEntity(
                message.messageId,
                message.sender,
                message.content,
                message.created,
                chatId
        );
    }

}
