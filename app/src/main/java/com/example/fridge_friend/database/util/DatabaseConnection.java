package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Contract;

public class DatabaseConnection {

    private final String userId;
    private final com.google.firebase.database.DatabaseReference reference;

    private DatabaseConnection(@NonNull String userId, com.google.firebase.database.DatabaseReference ref) {
        this.userId = userId;
        this.reference = ref;
    }

    @NonNull
    public String getUid() {
        return userId;
    }

    public com.google.firebase.database.DatabaseReference getDatabase() {
        return reference;
    }

    @NonNull
    @Contract(" -> new")
    public static DatabaseConnection getConnection() throws IllegalStateException {
        if (FirebaseAuth.getInstance().getUid() == null) {
            throw new IllegalStateException("Must be logged in to access the Firebase DB");
        }
        return new DatabaseConnection(FirebaseAuth.getInstance().getUid(), FirebaseDatabase.getInstance().getReference());
    }

}
