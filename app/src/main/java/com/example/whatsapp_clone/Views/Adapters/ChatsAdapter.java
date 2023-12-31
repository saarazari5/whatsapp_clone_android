package com.example.whatsapp_clone.Views.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends
        RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private final List<Chat> mChats;
    WeakReference<MutableLiveData<Chat>> weakSelectedChatMutable;

    public ChatsAdapter(List<Chat> chats, MutableLiveData<Chat> mChat) {
        this.mChats = chats;
        weakSelectedChatMutable = new WeakReference<>(mChat);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.chat_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mChats == null) {return;}
        Chat chat = mChats.get(position);
        if(chat == null){ return; }
        User currentUser = Repository.getInstance().getCurrentUser(); // messages empty but users have info
        User otherUser;

        if(Objects.equals(currentUser.username, chat.users.get(0).username)) {
            otherUser = chat.users.get(1);
        }else {
            otherUser = chat.users.get(0);
        }

        holder.itemView.setOnClickListener(view -> weakSelectedChatMutable.get().postValue(chat));
        holder.chatNameTxt.setText(otherUser.displayName);
        if (chat.lastMessage != null) {
            if(chat.lastMessage.content != null) {
                holder.lastMsgTxt.setText(chat.lastMessage.content);
            } else {
                holder.lastMsgTxt.setText("");

            }
            if(chat.lastMessage.created != null) {
                holder.dateTxt.setText(Utils.formatDateTime(chat.lastMessage.created, false));
            }else {
                holder.dateTxt.setText("");
            }
        }

        Bitmap bitmap = Utils.getDecodedPic(otherUser.profilePic);
        if(bitmap == null) {return;}
        holder.chatProfile.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chatNameTxt;
        public TextView lastMsgTxt;

        public TextView dateTxt;
        public CircleImageView chatProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            chatNameTxt = itemView.findViewById(R.id.chat_name);
            lastMsgTxt = itemView.findViewById(R.id.last_msg);
            chatProfile = itemView.findViewById(R.id.circleImageView);
            dateTxt = itemView.findViewById(R.id.date_txt);
        }
    }
}