package com.example.fridge_friend.database.listener;

import com.google.firebase.database.annotations.NotNull;

/**
 * Base Listener from which all listeners db inherit
 */
public interface BaseListener {

    /**
     * Called when a db operation fails
     * @param e exception that caused the failure
     */
    void onFailure(@NotNull Exception e);

    /**
     * Called when a db operation is cancelled
     */
    void onCanceled();

}
