package com.example.whatsapp_clone.Views.Fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.Repository;

import java.util.List;

public class MessagesViewModel extends ViewModel {
    private final MutableLiveData<List<Message>> messagesMutableData = new MutableLiveData<>();
    private List<Message> messages;

    public MutableLiveData<List<Message>> getMessagesMutableData() {
        return messagesMutableData;
    }


    public MessagesViewModel() {
        Repository.getInstance();
    }

    public void loadMessages(int chatId, User user) {
        Repository
                .getInstance()
                .getMessages("", chatId, user, result -> {
                        if(result.isSuccess()) {
                            messages = result.getData();
                            messagesMutableData.postValue(messages);
                        }
                });
    }

    public void addMessages(String msg, int chatId) {
        Repository
                .getInstance()
                .addMessage("", msg, chatId, result -> {
                    if(result.isSuccess()) {
                        messages.add(result.getData());
                        messagesMutableData.postValue(messages);
                    }
                });
    }

    public void mockMessages() {
        messages = Utils.mockMessages();
        messagesMutableData.postValue(messages);

    }
}