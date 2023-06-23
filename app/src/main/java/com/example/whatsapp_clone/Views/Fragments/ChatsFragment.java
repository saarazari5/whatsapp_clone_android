package com.example.whatsapp_clone.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.Views.Adapters.ChatsAdapter;
import com.example.whatsapp_clone.Views.MainActivity;
import com.example.whatsapp_clone.databinding.FragmentChatsBinding;
import java.util.List;
import java.util.Objects;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatsViewModel mViewModel;
    private RecyclerView chatsRv;

    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);
        MainActivity activity = ((MainActivity) requireActivity());
        activity.state = MainActivity.State.CHATS;
        activity.invalidateOptionsMenu();
        activity.observers.add(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatsRv = binding.chats;
        handleChatRecyclerView();
        handleAddContactButton();
        setupObservers();

        //mViewModel.mockChats();
        mViewModel.fetchChats();
    }


    /**
     * handle Observers for mutable live data object
     */
    private void setupObservers() {
        final Observer<List<Chat>> chatsObserver = chats -> {
            chatsRv.setAdapter(new ChatsAdapter(chats,
                    mViewModel.getSelectedChatMutableData()));
            chatsRv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        };

        final Observer<Chat> selectedChatObserver = this::navigateToMessages;

        mViewModel.getChatsMutableData().observe(this.getViewLifecycleOwner(), chatsObserver);
        mViewModel.getSelectedChatMutableData().observe(this.getViewLifecycleOwner(), selectedChatObserver);
    }

    private void handleAddContactButton() {

        binding.fab.setOnClickListener(view1 -> {
            //create Alert for Add Contact
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            final View customLayout = getLayoutInflater().inflate(R.layout.add_contact_dialog, null);
            builder.setView(customLayout);
            AlertDialog dialog = builder.create();
            ImageView cancelBtn = customLayout.findViewById(R.id.cancel_btn);
            cancelBtn.setOnClickListener(view22 -> dialog.dismiss());
            Button addBtn = customLayout.findViewById(R.id.add_contact_btn);

            //handle Add ContactButton
            addBtn.setOnClickListener(view2 -> {
                EditText addContactEt = customLayout.findViewById(R.id.add_contact_et);
                //todo: consider add error handling
                mViewModel.createChat(addContactEt.getText().toString());
                dialog.dismiss();
            });
            dialog.show();
        });
    }

    private void handleChatRecyclerView() {
        chatsRv.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                if (dy<0 && !binding.fab.isShown())
                    binding.fab.show();
                else if(dy>0 && binding.fab.isShown())
                    binding.fab.hide();
                else if (dy == 0 && !binding.fab.isShown()) binding.fab.show();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState==RecyclerView.SCROLL_STATE_IDLE) {
                    binding.fab.show();
                }else if(newState==RecyclerView.SCROLL_STATE_DRAGGING){
                    binding.fab.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }


    private void navigateToMessages(Chat chat) {
        Bundle args = new Bundle();

        User currentUser = Repository.getInstance().getCurrentUser();
        User otherUser;

        if(Objects.equals(currentUser.username, chat.users.get(0).username)) {
            otherUser = chat.users.get(1);
        }else {
            otherUser = chat.users.get(0);
        }


        args.putString("current_chat_name", otherUser.username);
        args.putString("current_chat_displayName", otherUser.displayName);
        args.putString("current_chat_profilePic", otherUser.profilePic);
        args.putInt("current_chat_id", chat.chatId);

        Navigation.findNavController(binding
                .getRoot())
                .navigate(R.id.action_chatsFragment_to_messagesFragment,args);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}