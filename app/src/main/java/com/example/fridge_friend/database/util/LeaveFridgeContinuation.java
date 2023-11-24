package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

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
            updates.put("/fridgeMembers/" + fridgeName + "/" + uid, null);
            updates.put("/userAccess/" + uid + "/" + fridgeName, null);
            if (count == 1) {
                updates.put("/fridges" + fridgeName, null);
            }
            return root.updateChildren(updates);
        }
        // Return a Void task in case there's no work to do
        return Tasks.forResult(null);
    }

}
