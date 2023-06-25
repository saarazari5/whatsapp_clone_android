package com.example.whatsapp_clone;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.whatsapp_clone.Model.Adapters.MessageToMessageEntityAdapter;
import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.MessageEntity;
import com.example.whatsapp_clone.Model.Retrofit.CreateChatPOJO;
import com.example.whatsapp_clone.Model.Retrofit.HTTPClientDataSource;
import com.example.whatsapp_clone.Model.Room.RoomClientDataSource;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * important note about callback in this class... each callback will be called twice.
 * once when fetching from local db and other after performing snapshot update from the remote.
 */
public class Repository {
    private static Repository instance;
    // boiler plate , ignore it
    private WeakReference<LifecycleOwner> LOWeakReference;
    private WeakReference<Context> contextWeakReference;

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
        User.UserRegistration currentUser = new SPManager(contextWeakReference.get()).getUser("current_user");
        if (currentUser == null) {
            return null;
        }
        return new User(currentUser.username, currentUser.displayName, currentUser.profilePic);
    }

    public String getToken() {
        return new SPManager(contextWeakReference.get()).getString("token");
    }

    // temporary implementation
    public void deleteChat(String token, Integer chatId, CompletionBlock<Void> completionBlock) {
        this.httpClientDataSource.deleteChat(token, chatId, result -> {
            if (result.isSuccess()){
                roomClientDataSource.deleteChat(chatId);
            }
            completionBlock.onResult(result);
        });

    }

    public void createChat(String token, HashMap<String, String> username, CompletionBlock<Chat> completionBlock) {
        httpClientDataSource.createChat(token, username, result -> {
            if (result.isSuccess()) {
                CreateChatPOJO pojo = result.getData();
                List<User> temp = new ArrayList<>();
                temp.add(getCurrentUser());
                temp.add(pojo.addedUser);
                Chat chat = new Chat(pojo.id, null, temp);
                roomClientDataSource.insert(chat);
                completionBlock.onResult(new Result<>(true, chat, ""));
            } else {
                completionBlock.onResult(new Result<>(false, null, ""));
            }
        });
    }

    public void getMessages(String token, int chatId, CompletionBlock<List<MessageEntity>> completionBlock) {
        LiveData<List<MessageEntity>> mvd = roomClientDataSource.findMessages(chatId);
        mvd.observe(LOWeakReference.get(), messages -> {
            if (!messages.isEmpty()) {
                completionBlock
                        .onResult(new Result<>(true, messages, ""));
            }
            mvd.removeObservers(LOWeakReference.get());
            httpClientDataSource.getMessages(token, chatId, result -> {
                Result<List<MessageEntity>> res;
                if (result.isSuccess()) {
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
        LiveData<List<Chat>> chatsLD = roomClientDataSource.findChats();

        chatsLD.observe(LOWeakReference.get(), chats -> {
            if (!chats.isEmpty()) {
                completionBlock
                        .onResult(new Result<>(true, chats, ""));
            }
            chatsLD.removeObservers(LOWeakReference.get());
            syncChats(token, completionBlock);
        });
    }

    private void syncChats(String token, CompletionBlock<List<Chat>> completionBlock) {
        httpClientDataSource.getChats(token, result -> {
            if (result.isSuccess()) {
                roomClientDataSource.insert(result.getData().toArray(new Chat[0]));
            }
            completionBlock.onResult(result);
        });

    }

    public void addMessage(String token,
                           HashMap<String, String> msg,
                           int chatId,
                           CompletionBlock<MessageEntity> completionBlock) {
        httpClientDataSource.postMessage(token, msg, chatId, result -> {
            Result<MessageEntity> res;
            if (result.isSuccess()) {
                MessageEntity messageEntity = new MessageToMessageEntityAdapter().adapt(result.getData(), chatId);
                roomClientDataSource.insert(messageEntity);
                res = new Result<>(true, messageEntity, "");
            } else {
                res = new Result<>(false, null, result.getErrorMessage());
            }
            completionBlock.onResult(res);
        });
    }

    public void logOut() {
        roomClientDataSource.deleteAll();
        new SPManager(contextWeakReference.get()).clear();
    }


    public void createUser(User.UserRegistration user, CompletionBlock<Void> completionBlock) {
        httpClientDataSource.createUser(user, completionBlock);
    }

    public void setBaseURL(String baseURL) {
        if ((baseURL.isEmpty())) {
            return;
        }
        new SPManager(contextWeakReference.get()).putString("base_url", baseURL);
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

    public void handleLogin(String username, String password, String fcm ,CompletionBlock<String> completionBlock) {
        httpClientDataSource.loginUser(username, password, fcm ,completionBlock);
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

        Repository.getInstance()
                .contextWeakReference = new WeakReference<>(context);

        SPManager manager = new SPManager(context);
        String baseURL = manager.getString("base_url");
        if (baseURL == null) {
            Repository.getInstance().setBaseURL("http://10.0.2.2:5000/api/");
        } else {
            Repository.getInstance().setBaseURL(baseURL);
        }
    }
}
