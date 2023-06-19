package com.example.whatsapp_clone.Views.Fragments;

import android.preference.Preference;
import android.provider.SyncStateContract;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.data.LoginRepository;
import com.example.whatsapp_clone.data.Result;
import com.example.whatsapp_clone.data.model.LoggedInUser;
import com.example.whatsapp_clone.data.model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel class for the login screen.
 */
public class LoginViewModel extends ViewModel {
    private LoginRepository loginRepository;
    private UserPreference preferences;
    private MutableLiveData<User> loggedInUser = new MutableLiveData<>();
    private MutableLiveData<String> loggedInUserDetails = new MutableLiveData<>();
    public MutableLiveData<String> token = new MutableLiveData<>();
//    private MutableLiveData<Result.Error> loginError = new MutableLiveData<>();
//    private FirebaseAuth auth;

    public LoginViewModel() {
        loginRepository = new LoginRepository();
        apiService = ApiClient.createService(ApiService.class);
    }

    /**
     * Get the logged-in user LiveData.
     * @return LiveData representing the logged-in user.
     */
    public LiveData<User> getLoggedInUser() {
        return loggedInUser;
    }

    public LiveData<String> getLoggedInUserDetails() {
        return loggedInUserDetails;
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
     * @param username The user's username.
     * @param password The user's password.
     */
    public void loginUser(String username, String password) {
        // Connect to Firebase
//        auth = FirebaseAuth.getInstance();
//        auth.signInWithEmailAndPassword(username, password);
//        User user = new User(username, password, "picture");

        // Login request with username and password
        loginRepository.handleLogin(username, password, new LoginRepository.LoginCallback() {
            public void onSuccess(String token) {
                // Get the user
                loggedInUser.postValue(user);
                saveUserPreferences(loggedInUser.username, loggedInUser.displayName, loggedInUser.profilePic, token);
            }

            public void onError(Result.Error error) {
                // Fail login message
//                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
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
    private void saveUserPreferences(String username, String displayName, String profilePic, String token) {
        // Save the user preferences
        preferences.putBoolean("isLoggedIn", true);
        preferences.putString("username", username);
        preferences.putString("displayName", displayName);
        preferences.putString("profilePic", profilePic);
        preferences.putString("token", token);
        preferences.apply();
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
                    User user = response.body();
                    // Successfull login message
                    Toast.makeText(getContext(), "You are Logged in!", Toast.LENGTH_LONG).show();
                    // Get the user
                    loggedInUser.postValue(user);
                    // Navigate to chat screen or perform other actions
                } else {
                    // Handle API error
                    loginError.setValue(new Result.Error("Login failed"));
                }
            }

            @Override
            public void onFailure(Call<LoggedInUser> call, Throwable t) {
                // Handle network failure
                loginError.setValue(new Result.Error("Network error"));
            }
        });
    }
}
