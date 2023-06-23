package com.example.whatsapp_clone.Views.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.UserPreferences;
import com.example.whatsapp_clone.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A fragment that displays the login screen.
 */
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private EditText etUsername, etPassword;
    private final List<String> errors = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Create new view model for the login
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.initializePreferences(this);

        // Set the login title in the activity's action bar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Login");
            }
        }

        // Get the username and password inputs
        etUsername = binding.usernameInput;
        etPassword = binding.passwordInput;
        // Initialize the progress bar
        progressBar = binding.progressBar;

        // Toggle password visibility
        binding.showPasswordIcon.setOnClickListener(v -> {
            if (etPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                // Password is invisible - show it
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                // Change the icon
                binding.showPasswordIcon.setImageResource(R.drawable.ic_hide_password);
            } else {
                // Password is visible - hide it
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                // Change the icon
                binding.showPasswordIcon.setImageResource(R.drawable.ic_show_password_icon);
            }
        });

        // Set click listener for the login button
        binding.loginButton.setOnClickListener(v -> {
            // Get the username
            String username = etUsername.getText().toString();
            // Get the password
            String password = etPassword.getText().toString();

            // Validate form inputs
            boolean isFormValid = validateForm(username, password);
            if (!isFormValid) {
                Toast.makeText(getContext(), "Failed to Login", Toast.LENGTH_LONG).show();
                return;
            }

            // Make the progress bar visible
            progressBar.setVisibility(View.VISIBLE);
            // Handle login
            loginViewModel.loginUser(username, password);
            // Hide progress bar
            progressBar.setVisibility(View.GONE);
        });

        // Set click listener for the register link
        binding.registerLink.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });

        // Set listener for the logged user to navigate the chat page
        loginViewModel.getLoggedInUserLivedata().observe(this.getViewLifecycleOwner(), user -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_chatsFragment);
        });

        // Set listener for the error from the server if exists
        loginViewModel.getLoginError().observe(this.getViewLifecycleOwner(), result -> {
            String errorMsg = extractMessage(result.getErrorMessage());
            etUsername.setError(errorMsg);
            etPassword.setError(errorMsg);
            etUsername.requestFocus();
            etPassword.requestFocus();
        });
    }

    public String extractMessage(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has("message")) {
                return jsonObject.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Error: Unable to extract error message";
    }

    @Override
    public void onStart() {
        super.onStart();
        UserPreferences preferences = new UserPreferences(requireContext());
        // Check if the user is already logged in
        if (preferences.getBoolean("isLoggedIn")) {
            // Already logged in message
            Toast.makeText(getContext(), "Already logged in", Toast.LENGTH_LONG).show();
            // Finish the current activity
//            requireActivity().finish();
        } else {
            Toast.makeText(getContext(), "You can login now", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Validates the form inputs for username and password.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the form inputs are valid, false otherwise.
     */
    private boolean validateForm(String username, String password) {
        boolean isValid = true;

        // Validate username
        if (username.isEmpty()) {
//            errors.add("Username is required");
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            isValid = false;
        } else if (!isValidUsername(username)) {
//            errors.add("Invalid username format");
            etUsername.setError("Invalid username format");
            etUsername.requestFocus();
            isValid = false;
        }

        // Validate password
        if (password.isEmpty()) {
            errors.add("Password is required");
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            isValid = false;
        } else if (password.length() < 8) {
            errors.add("Password is less than 8 characters");
            etPassword.setError("Password is less than 8 characters");
            etPassword.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    /**
     * Validates if the given email is a valid username format.
     *
     * @param username The username to validate.
     * @return True if the username is a valid email format, false otherwise.
     */
    private boolean isValidUsername(String username) {
        // Use Android's built-in email pattern matcher to validate the email format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}