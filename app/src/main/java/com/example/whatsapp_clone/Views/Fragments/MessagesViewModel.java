package com.example.whatsapp_clone.Views.Fragments;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.Adapters.MessageToMessageEntityAdapter;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.Repository;

import java.util.HashMap;
import java.util.List;

public class MessagesViewModel extends ViewModel {
    private final MutableLiveData<List<MessageEntity>> messagesMutableData = new MutableLiveData<>();
    private List<MessageEntity> messages;

    public MutableLiveData<List<MessageEntity>> getMessagesMutableData() {
        return messagesMutableData;
    }


    public MessagesViewModel() {
        Repository.getInstance();
    }

    public void loadMessages(int chatId, User user) {
        Repository repository = Repository.getInstance();
        String token = repository.getToken();

       repository
                .getMessages(token, chatId, result -> {
                        if(result.isSuccess()) {
                            messages = result.getData();
                            messagesMutableData.postValue(messages);
                        }
                });
    }

    public void addMessages(String msg, int chatId) {
        HashMap<String, String> message = new HashMap<>();
        message.put("msg", msg);
        Repository repository = Repository.getInstance();
        String token = repository.getToken();

        repository
                .addMessage(token, message, chatId, result -> {
                    if(result.isSuccess()) {
                        messages.add(result.getData());
                        messagesMutableData.postValue(messages);
                    }
                });
    }

    public void deleteChat(Integer chatId, CompletionBlock<Void> completionBlock) {
        Repository repository = Repository.getInstance();
        String token = repository.getToken();

        repository.deleteChat(token, chatId,completionBlock);
    }


    public void mockMessages() {
        messages = new MessageToMessageEntityAdapter().adapt(Utils.mockMessages(),1);
        messagesMutableData.postValue(messages);

    }
}