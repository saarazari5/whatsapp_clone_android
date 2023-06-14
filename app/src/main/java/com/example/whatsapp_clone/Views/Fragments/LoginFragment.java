package com.example.whatsapp_clone.Views.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.databinding.FragmentLoginBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment that displays the login screen.
 */
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel loginViewModel;
    private List<String> errors = new ArrayList<>();
    private boolean isLoading = false;
    private boolean isAuthenticated = false;
    private boolean isPasswordVisible = false;
    private boolean formSubmitted = false;

    /**
     * Creates a new instance of LoginFragment.
     *
     * @return A LoginFragment instance.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        // Create new view model for the login
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observer
        Observer<User> userObserver = user -> {
            Bundle args = new Bundle();
            args.putString("current_chat_name", chat.users.get(0).username);
            args.putString("current_chat_image", chat.users.get(0).displayName);
            args.putInt("current_chat_id", chat.chatId);

            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_chatsFragment,args);
        };

        loginViewModel.getLoggedInUser().observe(this.getViewLifecycleOwner(), userObserver);




        Observer<String> tokenObserver = s -> {
            String username = binding.usernameInput.getText().toString();
            loginViewModel.getUserFromDB();
        };




        // Toggle password visibility
        binding.showPasswordIcon.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
        });

        // Set click listener for the login button
        binding.loginButton.setOnClickListener(v -> {
            // Handle login button click
            formSubmitted = true;   // set submitted to true
            // Get the username
            String username = binding.usernameInput.getText().toString();
            // Get the password
            String password = binding.passwordInput.getText().toString();

            // Validate form inputs
            boolean isFormValid = validateForm(username, password);
            if (!isFormValid) {
//                binding.errorText.setVisibility(View.VISIBLE);
                return;
            }

            // Handle login
            isLoading = true;
        });

//        binding.loginButton.setOnClickListener(v -> {
//            String username = etUsername.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
//
//            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
//                Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            progressBar.setVisibility(View.VISIBLE);
//            loginViewModel.loginUser(username, password);
//        });

        // Set click listener for the register link
        binding.registerLink.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }

    /**
     * Validates the form inputs for username and password.
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the form inputs are valid, false otherwise.
     */
    private boolean validateForm(String username, String password) {
        boolean isValid = true;

        // Validate username
        if (username.isEmpty()) {
            errors.add("Email is required");
            isValid = false;
        } else if (!isValidUsername(username)) {
            errors.add("Invalid email format");
            isValid = false;
        }

        // Validate password
        if (password.isEmpty()) {
            errors.add("Password is required");
            isValid = false;
        } else if (password.length() < 8) {
            errors.add("Password is less than 8 characters");
            isValid = false;
        }

        return isValid;
    }


    /**
     * Validates if the given email is a valid username format.
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