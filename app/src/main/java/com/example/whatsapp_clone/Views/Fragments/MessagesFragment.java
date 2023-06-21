package com.example.whatsapp_clone.Views.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Views.Adapters.MessagesAdapter;
import com.example.whatsapp_clone.Views.MainActivity;
import com.example.whatsapp_clone.databinding.FragmentMessagesBinding;

import java.util.List;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private MessagesViewModel mViewModel;

    private RecyclerView messagesRv;

    private User userSender;
    private int chatId;

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        MainActivity activity = ((MainActivity) requireActivity());
        activity.state = MainActivity.State.MESSAGES;
        mViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);

        assert getArguments() != null;
        this.userSender = new User(
                getArguments()
                .getString("current_chat_name"),
                getArguments().getString("current_chat_displayName"),
                getArguments().getString("current_chat_profilePic"));

        this.chatId = getArguments().getInt("current_chat_id");
        activity.didEnterMessageScreen(userSender, view -> {
            Navigation.findNavController(requireView()).navigate(R.id.action_messagesFragment_to_chatsFragment);
        });

        activity.invalidateOptionsMenu();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        messagesRv = binding.recyclerGchat;
        handleMessageRecyclerView();
        handleSendMessageButton();
        setupObservers();
        mViewModel.loadMessages(this.chatId, this.userSender);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupObservers() {
        final Observer<List<Message>> messagesObserver = messages -> {
            messagesRv.setAdapter(new MessagesAdapter(messages));
            messagesRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        };

        mViewModel.getMessagesMutableData().observe(this.getViewLifecycleOwner(), messagesObserver);

    }

    private void handleMessageRecyclerView() {


    }

    private void handleSendMessageButton() {
        binding.buttonGchatSend
                .setOnClickListener(view -> {
                    String msg = binding.editGchatMessage.getText().toString();
                    mViewModel.addMessages(msg,chatId);
                });
    }


}