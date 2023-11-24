package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

/**
 * Consolidated Firebase authentication check and Firebase db reference.
 * Gets around needing to null check in every function before making a db call that needs user id
 */
public class DatabaseConnection {

    private final String userId;
    private final com.google.firebase.database.DatabaseReference reference;

    private DatabaseConnection(@NonNull String userId, com.google.firebase.database.DatabaseReference ref) {
        this.userId = userId;
        this.reference = ref;
    }

    /**
     * Get the current users user id
     * @return user id of the current user
     */
    @NonNull
    public String getUid() {
        return userId;
    }

    /**
     * Get the firebase database instance
     * @return Firebase database instance reference
     * @see com.google.firebase.database.DatabaseReference
     */
    public com.google.firebase.database.DatabaseReference getDatabase() {
        return reference;
    }

    /**
     * Acquire an authenticated database connection to firebase.
     * This function checks Firebase.getInstance().getUid() doesn't return null before returning references to Firebase components.
     * This consolidates the null check while also clearing the warnings in android studio.
     * @return A DataBaseConnection with a reference to Firebase along with the current users id after checking the user is logged in.
     * @throws IllegalStateException if the user isn't logged into firebase
     */
    @NonNull
    @Contract(" -> new")
    public static DatabaseConnection getConnection() throws IllegalStateException {
        if (FirebaseAuth.getInstance().getUid() == null) {
            throw new IllegalStateException("Must be logged in to access the Firebase DB");
        }
        return new DatabaseConnection(FirebaseAuth.getInstance().getUid(), FirebaseDatabase.getInstance().getReference());
    }

}
