package com.example.whatsapp_clone.Views.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.Result;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.SPManager;

/**
 * ViewModel class for the login screen.
 */
public class LoginViewModel extends ViewModel {
    private final Repository repository;
    private SPManager preferences;
    private final MutableLiveData<Result<String>> loginError = new MutableLiveData<>();
    private final MutableLiveData<User>loggedInUserLivedata = new MutableLiveData<>();

    public LoginViewModel() {
        repository = Repository.getInstance();
    }

    public void initializePreferences(Fragment loginFragment) {
        this.preferences = new SPManager(loginFragment.requireContext());
    }

    /**
     * Get the login error LiveData.
     * @return LiveData representing the login error.
     */
    public MutableLiveData<Result<String>> getLoginError() {
        return loginError;
    }
    public MutableLiveData<User> getLoggedInUserLivedata(){return loggedInUserLivedata;}

    /**
     * Perform login with username and password.
     * @param username The user's username.
     * @param password The user's password.
     */
    public void loginUser(Context context, String username, String password) {
        SharedPreferences sp = context.getSharedPreferences("fcmTokens", Context.MODE_PRIVATE);
        String fcmToken = sp.getString("fcmToken", "");
        // Login request with username, password and the fcmToken
        repository.handleLogin(fcmToken, username, password, result -> {
            // Check if result is an error
            if (result.isSuccess()) {
                // Get the user and send it to the chat screen
                handleUser(username, password, result.getData(), fcmToken);
            } else {
                // Handle the error message
                String errorMsg = result.getErrorMessage();
                loginError.postValue(new Result<>(false, "ERROR", errorMsg));
            }
        });
    }

    /**
     * Get the user profile for the specified user ID.
     * @param username The user ID.
     */
    private void handleUser(String username,String password, String token, String fcmToken) {
        // Request user profile from the repository
        repository.getUser(username, token, result -> {
            // Get the user and send it to the chat screen
            if (result.isSuccess()) {
                User user = result.getData();
                saveUserPreferences(user.username, user.displayName, user.profilePic, password, token, fcmToken);
                loggedInUserLivedata.postValue(user);
            } else {
                // Handle the error message
                String errorMsg = result.getErrorMessage();
                loginError.postValue(new Result<>(false, "ERROR", errorMsg));
            }
        });
    }

    /**
     * Save the authentication token to local storage or preferences.
     * @param token The authentication token.
     */
    private void saveUserPreferences(String username, String displayName, String profilePic,
                                     String password, String token, String fcmToken) {

        User.UserRegistration userRegistration = new User.UserRegistration(username,password,displayName,profilePic);
        preferences.putUser(userRegistration, "current_user");
        // Save the user preferences
        preferences.putBoolean("isLoggedIn", true);
        preferences.putString("username", username);
        preferences.putString("displayName", displayName);
        preferences.putString("profilePic", profilePic);
        preferences.putString("token", token);
        preferences.putString("fcmToken", fcmToken);
        Log.i("token", token);
    }
}
