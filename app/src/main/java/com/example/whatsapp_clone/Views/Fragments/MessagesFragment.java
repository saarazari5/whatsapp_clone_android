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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Events.AddEvent;
import com.example.whatsapp_clone.Model.Events.AddMessageEvent;
import com.example.whatsapp_clone.Model.Events.DeleteEvent;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.Views.Adapters.MessagesAdapter;
import com.example.whatsapp_clone.Views.MainActivity;
import com.example.whatsapp_clone.databinding.FragmentMessagesBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private MessagesViewModel mViewModel;

    private RecyclerView messagesRv;

    private User userSender;
    private Integer chatId;

    boolean inDeletingChat = false;

    private ImageView deleteContactIV;

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }


    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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

        this.chatId =  Integer.valueOf(getArguments().getInt("current_chat_id"));
        activity.didEnterMessageScreen(userSender, view ->
                Navigation.findNavController(requireView()).navigate(R.id.action_messagesFragment_to_chatsFragment));

        activity.invalidateOptionsMenu();
        this.deleteContactIV = activity.getDeleteContactIV();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        messagesRv = binding.recyclerGchat;
        handleSendMessageButton();
        setupObservers();
        handleDeleteChatIV();
        mViewModel.loadMessages(this.chatId);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupObservers() {
        final Observer<List<MessageEntity>> messagesObserver = messages -> {
            if (messages.size() > 1) {
                if (messages.get(0).messageId > messages.get(1).messageId) {
                    Collections.reverse(messages);
                }
            }

            MessagesAdapter adapter = new MessagesAdapter(messages);
            messagesRv.setAdapter(adapter);
            messagesRv.setLayoutManager(new LinearLayoutManager(this.getContext()));

            // Scroll to the last item in the RecyclerView
            if (messages.size() > 0) {
                messagesRv.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        };

        mViewModel.getMessagesMutableData().observe(this.getViewLifecycleOwner(), messagesObserver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(AddMessageEvent event) {
        MessageEntity message = event.getMessage();
        mViewModel.loadMessages(this.chatId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(DeleteEvent event) {
        if(this.inDeletingChat) {return;}
        this.inDeletingChat = true;
        mViewModel.deleteChat(chatId, result -> {
            this.inDeletingChat = false;
            if (result.isSuccess()){
                Toast.makeText(requireContext(),
                        "deletion succeeded.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_messagesFragment_to_chatsFragment);

            }else{
                Toast.makeText(requireContext(),
                        "deletion failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handleDeleteChatIV() {
        if(this.inDeletingChat) {return;}
        this.inDeletingChat = true;
        this.deleteContactIV.setOnClickListener(v -> mViewModel.deleteChat(this.chatId, result -> {
            this.inDeletingChat = false;
            if (result.isSuccess()){
                Toast.makeText(requireContext(),
                        "deletion succeeded.", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_messagesFragment_to_chatsFragment);

            }else{
                Toast.makeText(requireContext(),
                        "deletion failed.", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void handleSendMessageButton() {
        binding.buttonGchatSend
                .setOnClickListener(view -> {
                    String msg = binding.editGchatMessage.getText().toString();
                    binding.editGchatMessage.setText("");
                    mViewModel.addMessages(msg,chatId);
                });
    }


}