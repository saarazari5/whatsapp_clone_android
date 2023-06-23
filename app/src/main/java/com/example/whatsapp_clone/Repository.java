package com.example.whatsapp_clone;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.whatsapp_clone.Model.Adapters.MessageToMessageEntityAdapter;
import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.Retrofit.CreateChatPOJO;
import com.example.whatsapp_clone.Model.Retrofit.HTTPClientDataSource;
import com.example.whatsapp_clone.Model.Room.RoomClientDataSource;
import com.example.whatsapp_clone.Model.Token;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.HEAD;

/**
 * Repository singleton , all Communication with ROOM and RETROFIT should be from this class
 * everything else will not be allowed!!!!
 * also you should work with the same classes for both services tou can either
 * 1) create an interface for DataSource and make HTTPClient and RoomClient , implement both ,
 * the repo can use strategy pattern to switch between them
 * 2) make sure all the data classes are support both Room and Retro aka USER,MESSAGE,CHAT
 */
public class Repository {
    private static Repository instance;
    // boiler plate , ignore it
    private WeakReference<LifecycleOwner> LOWeakReference;

    private final HTTPClientDataSource httpClientDataSource;
    private RoomClientDataSource roomClientDataSource;

    // Private constructor to prevent instantiation from other classes
    private Repository() {
        httpClientDataSource = new HTTPClientDataSource();
    }

    /**
     * use shared preferences to get the current logged in user
     * todo: replace the code with Ido sharedPF class
     *
     * @return current logged in user
     */
    public User getCurrentUser() {
        return new User.Mock();
    }

    public void createChat(String token, String userName, CompletionBlock<Chat> completionBlock) {
        httpClientDataSource.createChat(token, userName, result -> {
            if (result.isSuccess()) {
                CreateChatPOJO pojo = result.getData();
                List<User> temp = new ArrayList<>();
                temp.add(pojo.addedUser);
                temp.add(getCurrentUser());
                Chat chat = new Chat(pojo.id, null, temp);
                roomClientDataSource.insert(chat);
                completionBlock.onResult(new Result<>(true, chat, ""));
            } else {
                completionBlock.onResult(new Result<>(false, null, ""));
            }
        });
    }

    public void getMessages(String token, int chatId, User user, CompletionBlock<List<MessageEntity>> completionBlock) {
            roomClientDataSource.findMessages(chatId)
                    .observe(LOWeakReference.get(), messages -> {
                        if (!messages.isEmpty()) {
                            completionBlock
                                .onResult(new Result<>(true, messages, ""));
                        return;
                    }
                        httpClientDataSource.getMessages(token, chatId, result -> {
                            Result<List<MessageEntity>> res;
                            if(result.isSuccess()) {
                                List<MessageEntity> messageEntities = new MessageToMessageEntityAdapter()
                                                                     .adapt(result.getData(), chatId);
                                roomClientDataSource.insert(messageEntities.toArray(new MessageEntity[0]));
                                res = new Result<>(true, messageEntities, "");
                            } else {
                                res = new Result<>(result.isSuccess(), null, result.getErrorMessage());
                            }
                            completionBlock.onResult(res);
                        });
                    });
    }


    public void getChats(String token, CompletionBlock<List<Chat>> completionBlock) {
        roomClientDataSource.findChats()
                .observe(LOWeakReference.get(), chats -> {
                    if (!chats.isEmpty()) {
                        completionBlock
                                .onResult(new Result<>(true, chats, ""));
                        return;
                    }
                    httpClientDataSource.getChats(token, result -> {
                        if (result.isSuccess()) {
                            roomClientDataSource.insert(result.getData().toArray(new Chat[0]));
                        }
                        completionBlock.onResult(result);
                    });
                });
    }

    public void addMessage(String token,
                           String msg,
                           int chatId,
                           CompletionBlock<MessageEntity> completionBlock){
        httpClientDataSource.postMessage(token, msg, chatId, result -> {
            Result<MessageEntity> res;
            if (result.isSuccess()) {
                MessageEntity messageEntity = new MessageToMessageEntityAdapter().adapt(result.getData(), chatId);
                roomClientDataSource.insert(messageEntity);
                res = new Result<>(true, messageEntity,"");
            } else {
                res = new Result<>(false, null, result.getErrorMessage());
            }
            completionBlock.onResult(res);
        });
    }

    public void logOut() {
        //todo: delete all room schemes and delete all cache related to current user
    }


    public void createUser(User.UserRegistration user, CompletionBlock<Void> completionBlock) {
        httpClientDataSource.createUser(user, completionBlock);
    }

    public void setBaseURL(String baseURL) {
        if ((baseURL.isEmpty())) {return;}
        httpClientDataSource.setBaseUrl(baseURL);
    }

    // Static method to get the instance
    public static Repository getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }

    public void handleLogin(String username, String password, CompletionBlock<String> completionBlock) {
        httpClientDataSource.loginUser(username, password, completionBlock);
    }

    public void getUser(String username, String token, CompletionBlock<User> completionBlock) {
        httpClientDataSource.getUserDetails(token, username, completionBlock);
    }

    public static void init(Context context, LifecycleOwner lifecycleOwner) {
        Repository
                .getInstance()
                .roomClientDataSource = new RoomClientDataSource(context);

        Repository.getInstance()
                .LOWeakReference = new WeakReference<>(lifecycleOwner);
    }
}
