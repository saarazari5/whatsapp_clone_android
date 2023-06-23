package com.example.whatsapp_clone.Views.Fragments;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.ValidationTester;
import com.example.whatsapp_clone.Views.MainActivity;
import com.example.whatsapp_clone.databinding.FragmentRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private Button registerBtn;
    private RegisterViewModel mViewModel;
    private Boolean isRegistrationSucceed;
    private Button uploadPictureBtn;

    private ActivityResultLauncher<Intent> imagePickerLauncher;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        this.binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View rootView = this.binding.getRoot();
        MainActivity activity = ((MainActivity) requireActivity());
        activity.state = MainActivity.State.REGISTER;
        activity.invalidateOptionsMenu();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.registerBtn.setOnClickListener(v -> {
            String userEmail = binding.emailInput.getText().toString();
            String userDisplayName = binding.displayNameInput.getText().toString();
            String userPassword = binding.passwordInput.getText().toString();
            String userPasswordConfirmation = binding.passwordConfirmationInput.getText().toString();

            this.mViewModel.registerUser(userEmail, userDisplayName, userPassword, userPasswordConfirmation, new RegistrationCallback() {
                @Override
                public void onRegistrationSuccess() {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_registerFragment_to_loginFragment);
                }
                @Override
                public void onRegistrationFailure() {
                    Toast.makeText(requireContext(),
                            "This email is already taken.", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onServerConnectionFailure(){
                    Toast.makeText(requireContext(),
                            "Failed to connect to the server.", Toast.LENGTH_SHORT).show();
                }
            });
        });
        setProfilePicUploadingLogic();
        setErrMsgDisappearLogic();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getErrorSetLiveData().observe(getViewLifecycleOwner(), inputErrors -> {
            for (RegisterViewModel.InputError inputError : inputErrors) {
                switch (inputError) {
                    case INVALID_EMAIL:
                        binding.emailInputLayout.setError(inputError.getErr());
                        break;
                    case INVALID_DISPLAYNAME_LENGTH:
                    case INVALID_DISPLAYNAME_CHAR:
                        binding.displayNameInputLayout.setError(inputError.getErr());
                        break;
                    case INVALID_PASSWORD_LENGTH:
                    case INVALID_PASSWORD_CHAR:
                        binding.passwordInputLayout.setError(inputError.getErr());
                        break;
                    case PASSWORDS_DONT_MATCH:
                        binding.passwordConfirmationInputLayout.setError(inputError.getErr());
                        break;
                }
                mViewModel.removeError(inputError);
            }
        });
        mViewModel.getProfilePictureLiveData().observe(getViewLifecycleOwner(), bitmap -> {
            ImageView profileImageView = binding.profileImageView;
            profileImageView.setImageBitmap(bitmap);
            profileImageView.setVisibility(View.VISIBLE);
        });
    }

    private void setProfilePicUploadingLogic() {
        this.uploadPictureBtn = binding.uploadPictureBtn;

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData();
                    mViewModel.handleImageSelection(requireContext().getContentResolver(), imageUri); // Call the handleImageSelection method
                }
            }
        });

        uploadPictureBtn.setOnClickListener(v -> {
            uploadPictureBtn.setText(R.string.change_profile_picture);
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);

        });

    }

    /**
     * Making the error messages disappear from the edit text components when the user tap on them.
     */
    private void setErrMsgDisappearLogic() {

        binding.emailInput.setOnClickListener(v -> {
            binding.emailInputLayout.setError(null);
        });
        binding.emailInputLayout.setOnClickListener(v -> {
            binding.emailInputLayout.setError(null);
        });

        binding.displayNameInput.setOnClickListener(v -> {
            binding.displayNameInputLayout.setError(null);
        });
        binding.displayNameInputLayout.setOnClickListener(v -> {
            binding.displayNameInputLayout.setError(null);
        });

        binding.passwordInput.setOnClickListener(v -> {
            binding.passwordInputLayout.setError(null);
        });
        binding.passwordInputLayout.setOnClickListener(v -> {
            binding.passwordInputLayout.setOnClickListener(null);
        });

        binding.passwordConfirmationInput.setOnClickListener(v -> {
            binding.passwordConfirmationInputLayout.setError(null);
        });
        binding.passwordConfirmationInputLayout.setOnClickListener(v -> {
            binding.passwordConfirmationInputLayout.setError(null);
        });

    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //indicating that the binding object is no longer needed and can be garbage collected.
        binding = null;
    }

    public interface RegistrationCallback {
        void onRegistrationSuccess();

        void onRegistrationFailure();

        void onServerConnectionFailure();
    }

}