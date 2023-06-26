package com.example.whatsapp_clone.Views;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.whatsapp_clone.Model.Delegates.SearchQueryObserver;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.SPManager;
import com.example.whatsapp_clone.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public State state = State.LOGIN;

    public enum State {
        CHATS,
        MESSAGES,
        LOGIN,
        REGISTER
    }

    private ImageView deleteContactIV;
    private ActivityMainBinding binding;

    private TextView toolbarTitle;
    public ArrayList<SearchQueryObserver> observers = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPManager spManager = new SPManager(this);
        boolean isNight = spManager.getBoolean("is_night_mode");
        if(isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Repository.init(getApplicationContext(), this);
        toolbarTitle = binding.myToolbar.findViewById(R.id.toolbar_title);
        this.deleteContactIV = binding.deleteContactBtn;

        askNotificationPermission();
        if(Utils.isSettingsOpen) {
            showSettingsBottomSheet();
        }

        handleFirebaseFCM();
    }

    private void handleFirebaseFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) { return; }
                    // Get new FCM registration token
                    String token = task.getResult();
                    new SPManager(this).putString("fcm_token", token);

                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        switch (state) {
            case CHATS:
                menuInflater.inflate(R.menu.chats_menu, menu);
                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView =
                        (SearchView) searchItem.getActionView();
                initSearchViewObserver(searchView);
                handleToolbarForChat();
                return true;
            case LOGIN:
                menuInflater.inflate(R.menu.default_menu, menu);
                handleToolbarForLogin();
                return true;
            case MESSAGES:
                menuInflater.inflate(R.menu.default_menu, menu);
                handleToolbarForMessages();
                return true;

            case REGISTER:
                menuInflater.inflate(R.menu.default_menu, menu);
                handleToolbarForRegister();
                return true;
        }
        return true;
    }

    private void handleToolbarForChat() {
        binding.userProfile.setVisibility(View.INVISIBLE);
        binding.backBtn.setVisibility(View.INVISIBLE);
        binding.deleteContactBtn.setVisibility(View.INVISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.chat_title);
    }

    private void handleToolbarForRegister() {
        binding.userProfile.setVisibility(View.GONE);
        binding.backBtn.setVisibility(View.GONE);
        binding.deleteContactBtn.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.register_title);

    }

    private void handleToolbarForLogin() {
        binding.userProfile.setVisibility(View.GONE);
        binding.backBtn.setVisibility(View.GONE);
        binding.deleteContactBtn.setVisibility(View.GONE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.login_title);
    }

    public void didEnterMessageScreen(User user, ImageView.OnClickListener onBackPressed) {
        binding.backBtn.setOnClickListener(onBackPressed);
        binding.userProfile.setImageBitmap(Utils.getDecodedPic(user.profilePic));
        toolbarTitle.setText(user.displayName);
    }

    private void handleToolbarForMessages() {
        binding.backBtn.setVisibility(View.VISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        binding.deleteContactBtn.setVisibility(View.VISIBLE);
        binding.userProfile.setVisibility(View.VISIBLE);
    }

    public void initSearchViewObserver(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                observers.forEach(searchQueryObserver -> searchQueryObserver.onQueryTextSubmit(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                observers.forEach(searchQueryObserver -> searchQueryObserver.onQueryTextChange( newText));
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            showSettingsBottomSheet();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showSettingsBottomSheet() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.settings_bottom_sheet, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Calculate the desired height for the bottom sheet
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int desiredHeight = (int) (screenHeight * 0.75);

        // Set the desired height for the bottom sheet
        ViewGroup.LayoutParams layoutParams = bottomSheetView.getLayoutParams();
        layoutParams.height = desiredHeight;
        bottomSheetView.setLayoutParams(layoutParams);

        // Optional: Customize the behavior of the bottom sheet
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setPeekHeight(desiredHeight);
        bottomSheetBehavior.setHideable(true);
        handleBottomSheetElements(bottomSheetDialog, bottomSheetView);

        addOverlay(bottomSheetDialog, bottomSheetView);
        bottomSheetDialog.setOnShowListener(dialogInterface -> Utils.isSettingsOpen = true);

        bottomSheetDialog.show();
    }

    private void addOverlay(BottomSheetDialog bottomSheetDialog, View bottomSheetView) {
        ViewGroup parentView = findViewById(android.R.id.content);
        View overlayView = new View(this);
        overlayView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlayView.setBackgroundColor(getResources().getColor(R.color.black));
         overlayView.setAlpha(0.5f);
        parentView.addView(overlayView);

        overlayView.setClickable(true);
        overlayView.setOnClickListener(null);

        // Set a listener to dismiss the bottom sheet and remove the overlay
        bottomSheetDialog.setOnDismissListener(dialog -> {
            parentView.removeView(overlayView);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Utils.isSettingsOpen = false;
        });

        // Set the appropriate flags for the activity window
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void handleBottomSheetElements(BottomSheetDialog dialog, View bottomSheetView) {
        SPManager spManager = new SPManager(this);
        //USER SECTION
        View userDetails = bottomSheetView.findViewById(R.id.current_user_details);
        CircleImageView userImage = bottomSheetView.findViewById(R.id.currentUserImg);
        TextView displayName = bottomSheetView.findViewById(R.id.tvUserDisplayName);
        User currentUser = Repository.getInstance().getCurrentUser();
        if(currentUser != null) {
            userDetails.setVisibility(View.VISIBLE);
            userImage.setImageBitmap(Utils.getDecodedPic(currentUser.profilePic));
            displayName.setText(currentUser.displayName);
        }else {
            userDetails.setVisibility(View.GONE);
        }


        //Dark mode section
        SwitchCompat darkModeSwitch = (SwitchCompat) bottomSheetView.findViewById(R.id.switchDarkMode);

        boolean isNight = spManager.getBoolean("is_night_mode");
        darkModeSwitch.setChecked(isNight);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                spManager.putBoolean("is_night_mode", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                spManager.putBoolean("is_night_mode", false);
            }
        });

        // setURL

        View setUrlView = bottomSheetView.findViewById(R.id.llChangeApi);
        EditText urlET = bottomSheetView.findViewById(R.id.set_url_et);
        setUrlView.setOnClickListener(view -> {
            if(urlET.getVisibility() == View.GONE) {
                urlET.setVisibility(View.VISIBLE);
                urlET.setAlpha(0f);
                urlET.animate()
                        .alpha(1f)
                        .setDuration(150)
                        .setListener(null);
            } else if(urlET.getVisibility() == View.VISIBLE) {
                urlET.animate()
                        .alpha(0f)
                        .setDuration(150)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //todo: did not handled url validation
                                Repository.getInstance()
                                                .setBaseURL(urlET.getText().toString());
                                urlET.setVisibility(View.GONE);
                            }
                        });
            }
        });

        //Logout view
        View logoutView = bottomSheetView.findViewById(R.id.logoutLayout);
        logoutView.setOnClickListener(view -> {
            Repository.getInstance().logOut();
            dialog.dismiss();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            navController.navigate(R.id.loginFragment);
        });

    }

    public ImageView getDeleteContactIV(){
        return this.deleteContactIV;
    }



    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(this, "please add notification from settings to allow awsome features", Toast.LENGTH_LONG).show();
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

}