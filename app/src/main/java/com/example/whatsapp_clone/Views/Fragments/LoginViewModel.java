package com.example.whatsapp_clone.Views.Fragments;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.Token;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.UserPreferences;

/**
 * ViewModel class for the login screen.
 */
public class LoginViewModel extends ViewModel {
    private final Repository repository;
    private UserPreferences preferences;
    private final MutableLiveData<Result<String>> loginError = new MutableLiveData<>();

    public LoginViewModel() {
        repository = Repository.getInstance();
    }

    public void initializePreferences(Fragment loginFragment) {
        this.preferences = new UserPreferences(loginFragment.requireContext());
    }

    /**
     * Get the login error LiveData.
     * @return LiveData representing the login error.
     */
    public MutableLiveData<Result<String>> getLoginError() {
        return loginError;
    }

    /**
     * Perform login with username and password.
     * @param username The user's username.
     * @param password The user's password.
     */
    public void loginUser(String username, String password) {
        // Login request with username and password
        repository.handleLogin(username, password, new CompletionBlock<Token>() {
            @Override
            public void onResult(Result<Token> result) {
                // Check if result is an error
                if (result.isSuccess()) {
                    // Get the user and send it to the chat screen
                    handleUser(username, result.getData());
                } else {
                    // Handle the error message
                    String errorMsg = result.getErrorMessage();
                    loginError.setValue(new Result<String>(false, "ERROR", errorMsg));
                }
            }
        });
//        repository.handleLogin(username, password, new Callback<Token>() {
//            public void onSuccess(String token) {
//                // Get the user and send it to the chat screen
//                handleUser(username, token);
//            }
//
//            public void onError(Result error) {
//                // Handle login error
//                loginError.setValue(error);
//            }
//        });
    }

    /**
     * Get the user profile for the specified user ID.
     * @param username The user ID.
     */
    private void handleUser(String username, Object token) {
        // Request user profile from the repository
        repository.getUser(username, (String) token, new CompletionBlock<User>() {
            @Override
            public void onResult(Result<User> result) {
                // Get the user and send it to the chat screen
                if (result.isSuccess()) {
                    User user = result.getData();
                    saveUserPreferences(user.username, user.displayName, user.profilePic, (String) token);
                } else {
                    // Handle the error message
                    String errorMsg = result.getErrorMessage();
                    loginError.setValue(new Result<String>(false, "ERROR", errorMsg));
                }
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
    }
}
