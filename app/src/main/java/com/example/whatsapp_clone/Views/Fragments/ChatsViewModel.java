package com.example.whatsapp_clone.Views.Fragments;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Delegates.SearchQueryObserver;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatsViewModel extends ViewModel implements SearchQueryObserver {
    private final MutableLiveData<List<Chat>> chatsMutableData = new MutableLiveData<>();
    private final MutableLiveData<Chat> selectedChatMutableLiveData = new MutableLiveData<>();
    private List<Chat> chats;
    private List<Chat> filteredChats;

    public MutableLiveData<List<Chat>> getChatsMutableData() {
        return chatsMutableData;
    }

    public MutableLiveData<Chat> getSelectedChatMutableData() {
        return selectedChatMutableLiveData;
    }


    public void mockChats() {
        chats = Utils.mockChats();
        filteredChats = new ArrayList<>(chats);
        chatsMutableData.postValue(chats);
    }

    public void fetchChats() {
        Repository repository = Repository.getInstance();
        String token = repository.getToken();

        repository
                .getChats(token, result -> {
                    if (result.isSuccess()) {
                        chats = result.getData();
                        filteredChats = new ArrayList<>(chats);
                        chatsMutableData.postValue(chats);
                    }
                });
    }

    public void createChat(String contact, ChatsFragment.AddContactCallBack addContactCallBack) {
        HashMap<String, String> username = new HashMap<>();
        username.put("username", contact);

        Repository repository = Repository.getInstance();
        String token = repository.getToken();
        repository
                .createChat(token, username, result -> {
                    if (result.isSuccess()) {
                        boolean shouldAdd = true;
                        for (Chat chat : chats) {
                            if (chat.users.get(0).username.equals(contact) ||
                                    chat.users.get(1).username.equals(contact)) {
                                shouldAdd = false;
                                break;
                            }
                        }

                        if (!shouldAdd) {
                            return;
                        }
                        chats.add(result.getData());
                        filteredChats.add(result.getData());
                        chatsMutableData.postValue(filteredChats);
                        addContactCallBack.onContactAdded();

                    } else {
                        addContactCallBack.onInvalidContact();
                    }
                });
    }

    @Override
    public void onQueryTextSubmit(String query) {

        if (query.isEmpty()) {
            chatsMutableData.postValue(chats);
        } else {

            filteredChats = filteredChats.stream()
                    .filter(chat -> {
                        User currentUser = Repository.getInstance().getCurrentUser();
                        User otherUser;

                        if (Objects.equals(currentUser.username, chat.users.get(0).username)) {
                            otherUser = chat.users.get(1);
                        } else {
                            otherUser = chat.users.get(0);
                        }
                        return otherUser.displayName.contains(query);
                    })
                    .collect(Collectors.toList());
            chatsMutableData.postValue(filteredChats);
        }
    }

    @Override
    public void onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            filteredChats = new ArrayList<>(chats);
            chatsMutableData.postValue(chats);
        } else {
            ArrayList<Chat> newFilteredChats = new ArrayList<>();
            for (Chat chat : filteredChats) {
                User currentUser = Repository.getInstance().getCurrentUser();
                User otherUser;

                if (Objects.equals(currentUser.username, chat.users.get(0).username)) {
                    otherUser = chat.users.get(1);
                } else {
                    otherUser = chat.users.get(0);
                }

                if (otherUser.displayName.contains(newText)) {
                    newFilteredChats.add(chat);
                }
            }
            filteredChats = newFilteredChats;
            chatsMutableData.postValue(newFilteredChats);
        }
    }
}