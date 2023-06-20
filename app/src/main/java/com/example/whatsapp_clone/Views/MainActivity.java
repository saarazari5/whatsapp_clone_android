package com.example.whatsapp_clone.Views;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
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
import com.example.whatsapp_clone.R;
import com.example.whatsapp_clone.Repository;
import com.example.whatsapp_clone.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public State state = State.LOGIN;

    public enum State {
        CHATS,
        MESSAGES,
        LOGIN,
        REGISTER
    }

    private ActivityMainBinding binding;
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
//        testRequests();
    }


//    public void testRequests() {
//        Repository.getInstance()
//                .createUser(new User.UserRegistration("", "", "", ""), result -> {
//                    // do some shit
//                });
//    }

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
                return true;
            default:
                menuInflater.inflate(R.menu.default_menu, menu);
                return true;
        }
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