package com.example.whatsapp_clone.Views.Fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.data.LoginRepository;
import com.example.whatsapp_clone.data.Result;
import com.example.whatsapp_clone.data.model.LoggedInUser;
import com.example.whatsapp_clone.data.model.UserProfile;
import com.example.whatsapp_clone.network.ApiClient;
import com.example.whatsapp_clone.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel class for the login screen.
 */
public class LoginViewModel extends ViewModel {
    private LoginRepository loginRepository;
    private ApiService apiService;
    private MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    public MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<Result.Error> loginError = new MutableLiveData<>();

    public LoginViewModel() {
        loginRepository = new LoginRepository();
        apiService = ApiClient.createService(ApiService.class);
        Observer<String> tokenObserver = s -> {
            this.loginUser();
        };
    }

    /**
     * Get the logged-in user LiveData.
     * @return LiveData representing the logged-in user.
     */
    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Get the login error LiveData.
     * @return LiveData representing the login error.
     */
    public LiveData<Result.Error> getLoginError() {
        return loginError;
    }

    /**
     * Perform login with username and password.
     * @param username    The user's username.
     * @param password    The user's password.
     */
    public void loginUser(String username, String password) {
        // Check if user already logged in
        loginRepository.login(username, password, new LoginRepository.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                // Get the user

                loggedInUser.postValue(user);
                saveAuthToken(user.getToken());
                getUserProfile(user.getUserId());
                // Navigate to chat screen or perform other actions
            }

            @Override
            public void onError(Result.Error error) {
                // Handle login error
                loginError.setValue(error);
            }
        });
    }

    /**
     * Get the user profile for the specified user ID.
     * @param userId The user ID.
     */
    private void getUserProfile(String userId) {
        // Make an API call to get the user profile
        Call<UserProfile> call = apiService.getUserProfile(userId);
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    UserProfile userProfile = response.body();
                    // Handle user profile response
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                // Handle network failure
            }
        });
    }

    /**
     * Save the authentication token to local storage or preferences.
     * @param token The authentication token.
     */
    private void saveAuthToken(String token) {
        // Save the authentication token to local storage or preferences
        // You can use SharedPreferences or other storage mechanisms
    }

    /**
     * Send a login request to the server with the provided username and password.
     * @param username    The user's username.
     * @param password    The user's password.
     */
    private void sendLoginRequest(String username, String password) {
        // Make an API call for login
        Call<LoggedInUser> call = apiService.login(username, password);
        call.enqueue(new Callback<LoggedInUser>() {
            @Override
            public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
                if (response.isSuccessful()) {
                    LoggedInUser user = response.body();
                    // Handle successful login response
                } else {
                    // Handle API error
                }
            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                // Handle network failure
            }
        });
    }
}
