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

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.ValidationTester;
import com.example.whatsapp_clone.databinding.FragmentRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private Button registerBtn;
    private RegisterViewModel mViewModel;
    private EditText userEmailInput;
    private EditText userDisplayNameInput;
    private EditText userPasswordInput;
    private EditText userPasswordConfInput;

    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView profileImageView;
    private Button uploadPictureBtn;

    private String base64ProfilePic;
    private ActivityResultLauncher<Intent> imagePickerLauncher;


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        binding.registerBtn.setOnClickListener(v -> {
            handleRegistration();
        });
        setProfilePicUploadingLogic();
        setErrMsgDisappearLogic();
    }

    private void setProfilePicUploadingLogic(){
        profileImageView = binding.profileImageView;
        uploadPictureBtn = binding.uploadPictureBtn;
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData(); // retrieve the URI of the selected image
                    handleImageSelection(imageUri);
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
        /*
             to do:
             add error msg for profile pic
        */
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

    private void handleImageSelection(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            profileImageView.setImageBitmap(bitmap);
            base64ProfilePic = convertBitmapToBase64(bitmap);
            profileImageView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    /**
     * add validation logic to profile picture
     * limit the size of the email, username, password and stuff
     */
    public void handleRegistration() {

        String userEmail = binding.emailInput.getText().toString();
        String userDisplayName = binding.displayNameInput.getText().toString();
        String userPassword = binding.passwordInput.getText().toString();
        String userPasswordConfirmation = binding.passwordConfirmationInput.getText().toString();


        if (!ValidationTester.isValidEmail(userEmail)) {
            binding.emailInputLayout.setError("The email address you entered is not valid.");
        }

        int displayNameValidationResult = ValidationTester.isValidStrInput(userDisplayName);
        if (displayNameValidationResult == 1)
            binding.displayNameInputLayout.setError("The username has to have between 8 to 20 characters");
        else if (displayNameValidationResult == 2)
            binding.displayNameInputLayout.setError("The username can not have whitespace characters in it.");

        int passwordValidationResult = ValidationTester.isValidStrInput(userPassword);
        if (passwordValidationResult == 1)
            binding.passwordInputLayout.setError("The password has to have between 8 to 20 characters");
        else if (passwordValidationResult == 2)
            binding.passwordInputLayout.setError("The password can not have whitespace characters in it.");

        if (!ValidationTester.arePasswordsEqual(userPassword, userPasswordConfirmation))
            binding.passwordConfirmationInputLayout.setError("The passwords do not match.");

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //indicating that the binding object is no longer needed and can be garbage collected.
        binding = null;
    }


}