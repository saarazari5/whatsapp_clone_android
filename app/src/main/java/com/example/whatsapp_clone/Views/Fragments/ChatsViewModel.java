package com.example.whatsapp_clone.Views.Fragments;

import android.text.Editable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Delegates.SearchQueryObserver;
import com.example.whatsapp_clone.Model.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatsViewModel extends ViewModel implements SearchQueryObserver {
    private final MutableLiveData<List<Chat>> chatsMutableData = new MutableLiveData<>();
    private final MutableLiveData<Chat> selectedChatMutableLiveData = new MutableLiveData<>();
    private List<Chat> chats;
    private List<Chat> filteredChats;
    public MutableLiveData<List<Chat>> getChatsMutableData() { return chatsMutableData; }
    public MutableLiveData<Chat> getSelectedChatMutableData() { return selectedChatMutableLiveData; }


    public void mockChats() {
        chats = Utils.mockChats();
        filteredChats = chats;
        chatsMutableData.postValue(chats);
    }

    @Override
    public void onQueryTextSubmit(String query) {
        if(query.isEmpty()) {
            chatsMutableData.postValue(chats);
        }else {
            filteredChats = filteredChats.stream()
                    .filter(chat -> chat.users.get(0).displayName.contains(query))
                    .collect(Collectors.toList());
            chatsMutableData.postValue(filteredChats);
        }
    }

    @Override
    public void onQueryTextChange(String newText) {
        if(newText.isEmpty()) {
            filteredChats = chats;
            chatsMutableData.postValue(chats);
        }else {
            ArrayList<Chat> newFilteredChats = new ArrayList<>();
            for (Chat chat : filteredChats) {
                if(chat.users.get(0).displayName.contains(newText)) {
                   newFilteredChats.add(chat);
                }
            }
            filteredChats = newFilteredChats;
            chatsMutableData.postValue(newFilteredChats);
        }
    }

    public boolean addContact(String query) {
        return true;
    }
}