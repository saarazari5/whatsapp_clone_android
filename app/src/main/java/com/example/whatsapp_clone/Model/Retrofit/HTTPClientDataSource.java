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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HTTPClientDataSource {
    private HTTPClientService service;
   private String baseUrl = "http://10.0.2.2:5000/api/";
   // private String baseUrl = "http://172.18.70.214:5000/api/";

    public HTTPClientDataSource() {
        initService();
    }

    public void createUser(User.UserRegistration user, CompletionBlock<Void> completionBlock) {
        service.createUser(user)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            completionBlock.onResult(new Result<>(true, null, ""));
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
    public void loginUser(String username, String password, String fcmToken, CompletionBlock<String> completionBlock) {
        service.loginUser(fcmToken, username,password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()) {
                    String token = response.body();
                    assert token != null;
                    token = "Bearer " + token;
                    completionBlock.onResult(new Result<>(true, token, ""));
                } else {
                    String errorMessage;
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            errorMessage = errorBody.string();
                        } else {
                            errorMessage = "Unknown error occurred";
                        }
                    } catch (IOException e) {
                        errorMessage = "Error reading response body";
                    }
                    completionBlock.onResult(new Result<>(false, null, errorMessage));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                completionBlock.onResult(new Result<>(false, null, t.getMessage()));
            }
        });
    }


    public void getUserDetails(String token, String username, CompletionBlock<User> completionBlock) {
        service.getUserDetails(token, username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    completionBlock.onResult(new Result<>(true, user, ""));
                } else {
                    String errorMessage;
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            errorMessage = errorBody.string();
                        } else {
                            errorMessage = "Unknown error occurred";
                        }
                    } catch (IOException e) {
                        errorMessage = "Error reading response body";
                    }
                    completionBlock.onResult(new Result<>(false, null, errorMessage));
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                completionBlock.onResult(new Result<>(false, null, t.getMessage()));
            }
        });
    }


    public void getChats(String token, CompletionBlock<List<Chat>> completionBlock) {
        service.getChats(token)
                .enqueue(new Callback<List<Chat>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Chat>> call, @NonNull Response<List<Chat>> response) {
                        if (response.isSuccessful()) {
                            List<Chat> chats = response.body();
                            completionBlock.onResult(new Result<>(true, chats, ""));
                        } else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Chat>> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void createChat(String token, HashMap<String, String> username, CompletionBlock<CreateChatPOJO> completionBlock) {
        service.createChat(token, username)
                .enqueue(new Callback<CreateChatPOJO>() {
                    @Override
                    public void onResponse(@NonNull Call<CreateChatPOJO> call, @NonNull Response<CreateChatPOJO> response) {
                        if (response.isSuccessful()) {
                            CreateChatPOJO createChatPOJO = response.body();
                            completionBlock.onResult(new Result<>(true, createChatPOJO, ""));
                        } else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CreateChatPOJO> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void getMessages(String token, int id, CompletionBlock<List<Message>> completionBlock) {
        service.getMessages(token, id)
                .enqueue(new Callback<List<Message>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Message>> call, @NonNull Response<List<Message>> response) {
                        if (response.isSuccessful()) {
                            List<Message> messages = response.body();
                            completionBlock.onResult(new Result<>(true, messages, ""));
                        } else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

    public void postMessage(String token,
                            HashMap<String, String> msg,
                            int chatId,
                            CompletionBlock<Message> completionBlock) {

        service.postMessage(token, msg, chatId)
                .enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                        if (response.isSuccessful()) {
                            Message message = response.body();
                            completionBlock.onResult(new Result<>(true, message, ""));
                        } else {
                            completionBlock.onResult(new Result<>(false, null, ""));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                        completionBlock.onResult(new Result<>(false, null, t.getMessage()));
                    }
                });
    }

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


