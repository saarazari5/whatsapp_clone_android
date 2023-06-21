package com.example.whatsapp_clone.Views;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;

import com.example.whatsapp_clone.Model.Chat;
import com.example.whatsapp_clone.Model.Delegates.SearchQueryObserver;
import com.example.whatsapp_clone.Model.Message;
import com.example.whatsapp_clone.Model.Token;
import com.example.whatsapp_clone.Model.User;
import com.example.whatsapp_clone.Model.Utils.CompletionBlock;
import com.example.whatsapp_clone.Model.Utils.Result;
import com.example.whatsapp_clone.Model.Utils.Utils;
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public State state = State.REGISTER;

    public enum State {
        CHATS,
        MESSAGES,
        LOGIN,
        REGISTER
    }

    private ActivityMainBinding binding;

    private TextView toolbarTitle;
    public ArrayList<SearchQueryObserver> observers = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Repository.init(getApplicationContext(), this);
        toolbarTitle = binding.myToolbar.findViewById(R.id.toolbar_title);
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
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.chat_title);
    }

    private void handleToolbarForRegister() {
        binding.userProfile.setVisibility(View.INVISIBLE);
        binding.backBtn.setVisibility(View.INVISIBLE);
        toolbarTitle.setVisibility(View.VISIBLE);
        toolbarTitle.setText(R.string.register_title);

    }

    private void handleToolbarForLogin() {
        binding.userProfile.setVisibility(View.INVISIBLE);
        binding.backBtn.setVisibility(View.INVISIBLE);
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
            Toast.makeText(this, "in Settings", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}