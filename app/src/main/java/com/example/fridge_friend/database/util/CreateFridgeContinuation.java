package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.example.fridge_friend.database.errors.FridgeInUseException;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class CreateFridgeContinuation implements Continuation<DataSnapshot, Task<Void>> {

    private final String fridgeName;
    private final String uid;

    public CreateFridgeContinuation(String fridgeName, String uid) {
        this.fridgeName = fridgeName;
        this.uid = uid;
    }
    @Override
    public Task<Void> then(@NonNull Task<DataSnapshot> task) throws FridgeInUseException {
        DataSnapshot result = task.getResult();
        for(DataSnapshot fridge: result.getChildren()) {
            if (fridgeName.equals(fridge.getKey())) {
                throw new FridgeInUseException("Fridge "+ fridgeName + " not found");
            }
        }
        Map<String, Object> updates = new HashMap<>();
        DatabaseReference root = result.getRef().getRoot();
        updates.put("/fridgeMembers/" + fridgeName + "/" + uid, true);
        updates.put("/userAccess/" + uid + "/" + fridgeName, true);
        return root.updateChildren(updates);
    }
}
