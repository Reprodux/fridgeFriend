package com.example.fridge_friend.database.util;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListContinuation implements Continuation<DataSnapshot, Task<Map<String, String>>> {

    @Override
    public Task<Map<String,String>> then(@NonNull Task<DataSnapshot> task) {
        List<Task<DataSnapshot>> tasks = new ArrayList<>();
        DataSnapshot result = task.getResult();
        DatabaseReference ref = result.getRef();
        for(DataSnapshot snapshot: result.getChildren()) {
            String userId = snapshot.getValue(String.class);
            if (userId != null) {
                tasks.add(ref.child("users").child(userId).get());
            }
        }
        return Tasks.whenAllSuccess(tasks).continueWith(new ListMapping());
    }

    private static class ListMapping implements Continuation<List<Object>, Map<String,String>> {

        @Override
        public Map<String, String> then(@NonNull Task<List<Object>> task) {
            Map<String, String> result = new HashMap<>();
            List<Object> taskResult = task.getResult();
            for(Object obj: taskResult) {
                if (obj instanceof DataSnapshot) {
                    DataSnapshot ds = (DataSnapshot) obj;
                    String uid = ds.getKey();
                    if (uid != null) {
                        String name = uid;
                        if (ds.hasChild("name")) {
                            name = ds.child("name").getValue(String.class);
                        }
                        result.put(uid, name);
                    }
                }
            }
            return result;
        }
    }
}
