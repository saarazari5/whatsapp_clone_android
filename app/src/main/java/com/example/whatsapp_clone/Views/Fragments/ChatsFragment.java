package com.example.whatsapp_clone.Views.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.databinding.FragmentChatsBinding;
import com.example.whatsapp_clone.databinding.FragmentMessagesBinding;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;
    private ChatsViewModel mViewModel;

    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChatsViewModel.class);

        binding.button.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.action_chatsFragment_to_messagesFragment));

    }

}