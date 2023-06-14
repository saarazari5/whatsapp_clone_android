package com.example.whatsapp_clone.Model.Utils;

public interface CompletionBlock<T> {
    void onResult(Result<T> result);
}
