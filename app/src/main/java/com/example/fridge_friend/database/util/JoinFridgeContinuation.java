package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.example.fridge_friend.database.errors.FridgeNotFoundException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Task continuation for joining fridges
 * Checks if fridge exists before attempting to join without blocking the main thread
 */
public class JoinFridgeContinuation implements Continuation<DataSnapshot, Task<Void>> {

    private final String fridgeName;
    private final String uid;

    /**
     * Instantiates a new Join fridge continuation.
     *
     * @param fridgeName the fridge name
     * @param uid        the uid
     */
    public JoinFridgeContinuation(String fridgeName, String uid) {
        this.fridgeName = fridgeName;
        this.uid = uid;
    }
    @Override
    public Task<Void> then(@NonNull Task<DataSnapshot> task) throws FridgeNotFoundException {
        DataSnapshot result = task.getResult();
        for(DataSnapshot fridge: result.getChildren()) {
            if (fridgeName.equals(fridge.getKey())) {
                Map<String, Object> updates = new HashMap<>();
                DatabaseReference root = result.getRef().getRoot();
                updates.put("/fridgeMembers/" + fridgeName + "/" + uid, true);
                updates.put("/userAccess/" + uid + "/" + fridgeName, true);
                return root.updateChildren(updates);
            }
        }
        throw new FridgeNotFoundException("Fridge "+ fridgeName + " not found");
    }
}
