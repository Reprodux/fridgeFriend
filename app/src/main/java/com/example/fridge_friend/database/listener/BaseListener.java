package com.example.fridge_friend.database.listener;

import com.google.firebase.database.annotations.NotNull;

public interface BaseListener {

    void onFailure(@NotNull Exception e);

    void onCanceled();

}
