package com.example.whatsapp_clone.Model.Delegates;

public interface SearchQueryObserver {
    public void onQueryTextSubmit(String query);
    public void onQueryTextChange(String newText);
}
