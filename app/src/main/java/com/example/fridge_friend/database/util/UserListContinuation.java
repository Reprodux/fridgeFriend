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

/**
 * Task Continuation for retrieving the user list.
 * Allows for retrieval of user names before returning to the main thread and calling the appropriate listener to notify the caller
 */
public class UserListContinuation implements Continuation<DataSnapshot, Task<Map<String, String>>> {

    @Override
    public Task<Map<String,String>> then(@NonNull Task<DataSnapshot> task) {
        List<Task<DataSnapshot>> tasks = new ArrayList<>();
        DataSnapshot result = task.getResult();
        DatabaseReference ref = result.getRef();
        for(DataSnapshot snapshot: result.getChildren()) {
            String userId = snapshot.getValue(String.class);
            if (userId != null) {
                tasks.add(ref.child("user").child(userId).child("name").get());
            }
        }
        return Tasks.whenAllSuccess(tasks).continueWith(new ListMapping());
    }

    /**
     * Private class for retrieving the user ids and producing the final map from the list of snapshots
     */
    private static class ListMapping implements Continuation<List<Object>, Map<String,String>> {

        @Override
        public Map<String, String> then(@NonNull Task<List<Object>> task) {
            Map<String, String> result = new HashMap<>();
            List<Object> taskResult = task.getResult();
            for(Object obj: taskResult) {
                if (obj instanceof DataSnapshot) {
                    DataSnapshot ds = (DataSnapshot) obj;
                    DatabaseReference parent = ds.getRef().getParent();
                    if (parent != null) {
                        String uid = parent.getKey();
                        if (uid != null) {
                            String name = ds.getValue(String.class);
                            if (name == null || name.equals("")) {
                                name = uid;
                            }
                            result.put(uid, name);
                        }
                    }
                }
            }
            return result;
        }
    }
}
