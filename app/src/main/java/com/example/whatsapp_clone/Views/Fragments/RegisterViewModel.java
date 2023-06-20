package com.example.whatsapp_clone.Views.Fragments;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp_clone.Model.Retrofit.HTTPClientDataSource;
import com.example.whatsapp_clone.ValidationTester;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class RegisterViewModel extends ViewModel {
    private HTTPClientDataSource source;
    private String base64ProfilePic;
    private MutableLiveData<Bitmap> profilePictureLiveData;
    private MutableLiveData<Set<InputError>> errorSetLiveData;

    enum InputError {
        INVALID_DISPLAYNAME_LENGTH("The username has to have between 8 to 20 characters"),
        INVALID_DISPLAYNAME_CHAR("The username can not have whitespace characters in it."),
        INVALID_EMAIL("The email address you entered is not valid."),
        INVALID_PASSWORD_LENGTH("The password has to have between 8 to 20 characters"),
        INVALID_PASSWORD_CHAR("The password can not have whitespace characters in it."),
        PASSWORDS_DONT_MATCH("The passwords do not match."),
        NO_PROFILE_PIC("You have to upload a profile picture."),
        PROFILE_PIC_IS_TOO_BIG("The image you picked is too big.");

        private String err;

        InputError(String hexCode) {
            this.err = hexCode;
        }

        public String getErr() {
            return err;
        }
    }

    public RegisterViewModel() {
        source = new HTTPClientDataSource();
        profilePictureLiveData = new MutableLiveData<>();
        errorSetLiveData = new MutableLiveData<>();
        errorSetLiveData.setValue(new HashSet<>());
    }


    public MutableLiveData<Set<InputError>> getErrorSetLiveData() {
        return errorSetLiveData;
    }

    public void addError(InputError error) {
        Set<InputError> errorSet = errorSetLiveData.getValue();
        if (errorSet != null) {
            errorSet.add(error);
            errorSetLiveData.setValue(errorSet);
        }
    }

    public void removeError(InputError error) {
        Set<InputError> errorSet = errorSetLiveData.getValue();
        if (errorSet != null) {
            errorSet.remove(error);
            errorSetLiveData.setValue(errorSet);
        }
    }
    public LiveData<Bitmap> getProfilePictureLiveData() {
        return profilePictureLiveData;
    }


    public void handleImageSelection(ContentResolver contentResolver, Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
            profilePictureLiveData.setValue(bitmap);
            this.base64ProfilePic = convertBitmapToBase64(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            //Todo: handle error
        }
    }

    public String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public void registerUser(String userEmail, String userDisplayName, String userPassword, String userPasswordConfirmation) {
        // Perform necessary validations on user inputs
//        String userEmail = binding.emailInput.getText().toString();
//        String userDisplayName = binding.displayNameInput.getText().toString();
//        String userPassword = binding.passwordInput.getText().toString();
//        String userPasswordConfirmation = binding.passwordConfirmationInput.getText().toString();


        if (!ValidationTester.isValidEmail(userEmail)) {
            addError(InputError.INVALID_EMAIL);
        }

        int displayNameValidationResult = ValidationTester.isValidStrInput(userDisplayName);
        if (displayNameValidationResult == 1)
            addError(InputError.INVALID_DISPLAYNAME_LENGTH);
        else if (displayNameValidationResult == 2)
            addError(InputError.INVALID_DISPLAYNAME_CHAR);

        int passwordValidationResult = ValidationTester.isValidStrInput(userPassword);
        if (passwordValidationResult == 1)
            addError(InputError.INVALID_PASSWORD_LENGTH);
        else if (passwordValidationResult == 2)
            addError(InputError.INVALID_PASSWORD_CHAR);
        if (!ValidationTester.arePasswordsEqual(userPassword, userPasswordConfirmation))
            addError(InputError.PASSWORDS_DONT_MATCH);

        // to do:
        /**
         * add validations tests for profile pic
         */


        // Convert profile picture to base64
//        String base64ProfilePic = convertBitmapToBase64(profilePicture);


        // Create a User object
//        User user = new User(userEmail, userDisplayName, userPassword, base64ProfilePic);

        // Call the HTTPClientDataSource method to create the user
//        source.createUser(user, new CompletionBlock<Void>() {
//            @Override
//            public void onResult(Result<Void> result) {
//                // Handle the result, update the UI accordingly
//                if (result.isSuccess()) {
//                    // User registration success, perform necessary actions
//                } else {
//                    // User registration failed, show an error message or take appropriate action
//                }
//            }
//        });
    }


}
