package com.example.whatsapp_clone.Model.Retrofit;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.Token;
import com.example.whatsapp_clone.Model.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface HTTPClientService {
    @POST("Users")
    Call<Void> createUser(@Body User.UserRegistration user);

    @POST("Tokens")
    Call<String> loginUser(@Body HashMap<String,String> params, @Header("fcmToken") String fcmToken);


    @GET("Users/{username}")
    Call<User> getUserDetails(@Header("Authorization") String token, @Path("username") String username);

    @GET("Chats")
    Call<List<Chat>> getChats(@Header("Authorization") String token);

    @POST("Chats")
    Call<CreateChatPOJO> createChat(@Header("Authorization") String token, @Body HashMap<String, String> username);

    @POST("Chats/{id}/Messages")
    Call<Message> postMessage(@Header("Authorization") String token,
                                 @Body HashMap<String, String> msg,
                                 @Path("id") int chatId);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Header("Authorization") String token,
                                       @Path("id") int id);
    @DELETE("Chats/{id}")
    Call<Void> deleteChat(@Header("Authorization") String token, @Path("id") int chatId);

}
