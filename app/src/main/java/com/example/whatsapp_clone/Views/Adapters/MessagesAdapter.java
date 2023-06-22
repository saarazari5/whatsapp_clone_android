package com.example.whatsapp_clone.Views.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends
        RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final List<MessageEntity> messages;

    public MessagesAdapter(List<MessageEntity> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_msg, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_msg, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    @Override
    public int getItemViewType(int position) {
        MessageEntity message =  messages.get(position);

        if (message.sender.username
                .equals(Repository.getInstance().getCurrentUser().username)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ViewHolder holder, int position) {
        MessageEntity message = messages.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class ReceivedMessageHolder extends ChatsAdapter.ViewHolder {
        TextView messageText, timeText, nameText;
        CircleImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (CircleImageView) itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(MessageEntity message) {
            messageText.setText(message.content);

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.created));
            nameText.setText(message.sender.displayName);

            // Insert the profile image from the URL into the ImageView.
            profileImage.setImageBitmap(Utils.getDecodedPic(message.sender.profilePic));
        }
    }

    private static class SentMessageHolder extends ChatsAdapter.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(MessageEntity message) {
            messageText.setText(message.content);

            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.created));
        }
    }
}
