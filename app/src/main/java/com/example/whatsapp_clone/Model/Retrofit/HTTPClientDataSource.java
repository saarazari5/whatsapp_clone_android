package com.example.whatsapp_clone.Model.Retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.Token;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HTTPClientDataSource {
    private HTTPClientService service;
    private String baseUrl = "http://10.0.2.2:5000/api/";


    public HTTPClientDataSource() {
        initService();
    }

    public void  createUser(User.UserRegistration user , CompletionBlock<Void> completionBlock) {
        service.createUser(user)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if(response.isSuccessful()) {
                            completionBlock.onResult(new Result<>(true, null,""));
                        } else {
                            completionBlock.onResult(new Result<>(false, null, String.valueOf(response.code())));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.d("test", "the registration failed!");
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void loginUser(String username, String password, CompletionBlock<Token> completionBlock) {
        service.loginUser(username,password)
                .enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                        if(response.isSuccessful()) {
                            Token token = response.body();
                            assert token != null;
                            token.token = "Bearer " + token.token;
                            completionBlock.onResult(new Result<>(true, token, ""));
                        } else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void getChats(String token, CompletionBlock<List<Chat>> completionBlock) {
        service.getChats(token)
                .enqueue(new Callback<List<Chat>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Chat>> call, @NonNull Response<List<Chat>> response) {
                        if(response.isSuccessful()) {
                            List<Chat> chats = response.body();
                            completionBlock.onResult(new Result<>(true, chats,""));
                        }else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Chat>> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void createChat(String token, String username, CompletionBlock<CreateChatPOJO> completionBlock) {
        service.createChat(token, username)
                .enqueue(new Callback<CreateChatPOJO>() {
                    @Override
                    public void onResponse(@NonNull Call<CreateChatPOJO> call, @NonNull Response<CreateChatPOJO> response) {
                        if(response.isSuccessful()) {
                            CreateChatPOJO createChatPOJO = response.body();
                            completionBlock.onResult(new Result<>(true, createChatPOJO,""));
                        }else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateChatPOJO> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void getMessages(String token, int id, CompletionBlock<List<Message>>completionBlock) {
        service.getMessages(token, id)
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Message>> call, @NonNull Response<List<Message>> response) {
                        if(response.isSuccessful()) {
                            List<Message> messages = response.body();
                            completionBlock.onResult(new Result<>(true, messages,""));
                        }else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void postMessage( String token,
                             String  msg,
                             int chatId,
                             CompletionBlock<Message> completionBlock) {

        service.postMessage(token,msg,chatId)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                        if(response.isSuccessful()) {
                            Message message = response.body();
                            completionBlock.onResult(new Result<>(true, message,""));
                        }else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }





//    private void initService() {
//        Retrofit retrofit = new Retrofit
//                .Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        this.service = retrofit.create(HTTPClientService.class);
//    }

    private void initService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.service = retrofit.create(HTTPClientService.class);
    }

    // getter and setter
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;

        //base URL can be changed from settings, therefore everytime we change it, recreate the service
        initService();
    }


}


