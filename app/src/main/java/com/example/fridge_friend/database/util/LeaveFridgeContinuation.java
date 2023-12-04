package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Task continuation for leaving fridges
 * Facilitates the deletion of fridge when all members leave the fridge without needing to block main thread
 */
public class LeaveFridgeContinuation implements Continuation<DataSnapshot, Task<Void>> {

    private final String fridgeName;
    private final String uid;

    public LeaveFridgeContinuation(String fridgeName, String uid) {
        this.fridgeName = fridgeName;
        this.uid = uid;
    }
    @Override
    public Task<Void> then(@NonNull Task<DataSnapshot> task) {
        DataSnapshot result = task.getResult();
        if (result != null) {
            long count = result.getChildrenCount();
            Map<String, Object> updates = new HashMap<>();
            DatabaseReference root = result.getRef().getRoot();
            if (count == 1) {
                // remove fridge
                updates.put("/fridges" + fridgeName, null);
                // Remove members node when count becomes 0
                updates.put("/fridgeMembers/" + fridgeName, null);
            } else {
                updates.put("/fridgeMembers/" + fridgeName + "/" + uid, null);
            }
            updates.put("/userAccess/" + uid + "/" + fridgeName, null);
            return root.updateChildren(updates);
        }
        // Return a Void task in case there's no work to do
        return Tasks.forResult(null);
    }

}
