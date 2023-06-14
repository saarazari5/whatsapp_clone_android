package com.example.whatsapp_clone.Model.Retrofit;

import androidx.annotation.NonNull;

import com.example.whatsapp_clone.Model.Token;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HTTPClientDataSource {
    private HTTPClientService service;
    private String baseUrl = "http://localhost:5000/api/";


    public HTTPClientDataSource() {
            initService();
    }


    public void createUser(User user , CompletionBlock<Void> completionBlock) {
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

    private void initService() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
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


