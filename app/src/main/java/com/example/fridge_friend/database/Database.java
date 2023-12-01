package com.example.fridge_friend.database;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.fridge_friend.database.errors.FridgeInUseException;
import com.example.fridge_friend.database.errors.FridgeNotFoundException;
import com.example.fridge_friend.database.listener.APIResultListener;
import com.example.fridge_friend.database.listener.FridgeListListener;
import com.example.fridge_friend.database.listener.ItemListener;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.database.listener.UserListListener;
import com.example.fridge_friend.database.listener.UserNameListener;
import com.example.fridge_friend.database.util.CreateFridgeContinuation;
import com.example.fridge_friend.database.util.DatabaseConnection;
import com.example.fridge_friend.database.util.JoinFridgeContinuation;
import com.example.fridge_friend.database.util.LeaveFridgeContinuation;
import com.example.fridge_friend.database.util.UserListContinuation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class for connecting to the firebase DB
 */
public class Database {

    /**
     * Get a list of the fridges a user has access to
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeListListener Listener that will receive the list of fridges once available
     * @see FridgeListListener#onListResult(List)
     */
    public static void listFridges(@NonNull Activity activity, @NonNull FridgeListListener fridgeListListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("userAccess").child(dbc.getUid()).get().addOnSuccessListener(activity, taskResult -> {
            List<String> fridgeNames = new ArrayList<>();
            for(DataSnapshot snapshot: taskResult.getChildren()) {
                fridgeNames.add(snapshot.getKey());
            }
            fridgeListListener.onListResult(fridgeNames);
        }).addOnCanceledListener(activity, fridgeListListener::onCanceled).addOnFailureListener(activity, fridgeListListener::onFailure);
    }

    /**
     * Retrieve the cached API result from our db
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param upc Universal Product Code (or more generally the key/name) of the API result to retrieve
     * @param apiResultListener Listener that will receive the API result once it's available
     * @see APIResultListener#onResult(String)
     */
    public static void getAPIResult(@NonNull Activity activity, @NonNull String upc, @NonNull APIResultListener apiResultListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("apiCache").child(upc).get().addOnSuccessListener(activity, taskResult -> {
            String result = taskResult.getValue(String.class);
            apiResultListener.onResult(result);
        }).addOnCanceledListener(activity, apiResultListener::onCanceled).addOnFailureListener(activity, apiResultListener::onFailure);
    }

    /**
     * Store an API result in our db for retrieval later
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param upc Universal Product Code (or more generally the key/name) to store the API result under
     * @param apiResult API result string to store
     * @param operationCompleteListener Listener that will be notified when the operation has completed
     * @see OperationCompleteListener#onSuccess()
     */
    public static void putAPIResult(@NonNull Activity activity, @NonNull String upc, String apiResult, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        DatabaseReference db = dbc.getDatabase();
        db.child("apiCache").child(upc).setValue(apiResult)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Attempt to join the fridge with the given name
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Name of the fridge to attempt to join
     * @param operationCompleteListener Listener that will be notified of the result of the operation.
     *                                  Operation may fail because the given fridge was not found, in which case a {@link FridgeNotFoundException} will be passed to onFailure
     * @see OperationCompleteListener#onSuccess()
     * @see OperationCompleteListener#onFailure(Exception)
     */
    public static void joinFridge(@NonNull Activity activity, @NonNull String fridgeName, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").get()
                .continueWithTask(new JoinFridgeContinuation(fridgeName, dbc.getUid()))
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Attempt to create a new fridge with a given name
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Name of the fridge to attempt to create
     * @param operationCompleteListener Listener that will be notified of the result of the operation.
     *                                  Operation may fail because the given name is already in use by another fridge, in which case a {@link FridgeInUseException} will be passed to onFailure
     * @see OperationCompleteListener#onSuccess()
     * @see OperationCompleteListener#onFailure(Exception)
     */
    public static void newFridge(@NonNull Activity activity, @NonNull String fridgeName, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").get()
                .continueWithTask(new CreateFridgeContinuation(fridgeName, dbc.getUid()))
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Attempt to leave the given fridge
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Name of the fridge to leave
     * @param operationCompleteListener Listener that will be notified of the result of the operation.
     *                                  Operation may silently succeed if the user isn't in the given fridge or the fridge doesn't exist even though no work was done.
     * @see OperationCompleteListener#onSuccess()
     */
    public static void leaveFridge(@NonNull Activity activity, @NonNull String fridgeName, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").child(fridgeName).get()
                .continueWithTask(new LeaveFridgeContinuation(fridgeName, dbc.getUid()))
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Get the list of items in the fridge.
     * Item ids are used as the keys in the resulting map
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Name of the fridge to get the items of
     * @param itemListener Listener to receive the list once it's been retrieved from the db
     * @see ItemListener#onResult(Map)
     */
    public static void getItems(@NonNull Activity activity, @NonNull String fridgeName, @NonNull ItemListener itemListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridges").child(fridgeName).get().addOnSuccessListener(activity, taskResult -> {
            Map<String, Item> result = taskResult.getValue(new GenericTypeIndicator<Map<String, Item>>() {});
            itemListener.onResult(result);
        }).addOnCanceledListener(activity, itemListener::onCanceled).addOnFailureListener(activity, itemListener::onFailure);
    }

    /**
     * Add an item to the fridge
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Fridge to add the item to
     * @param item Item to add to the fridge
     * @param operationCompleteListener Listener that will be notified when operation completes
     * @see OperationCompleteListener#onSuccess()
     */
    public static void addItem(@NonNull Activity activity, @NonNull String fridgeName, @NonNull Item item, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        DatabaseReference db = dbc.getDatabase();
        db.child("fridges").child(fridgeName).push().setValue(item)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Remove/delete an item from the fridge
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Fridge to remove the item from
     * @param itemId id of the item to remove
     * @param operationCompleteListener Listener that will be notified when operation completes
     * @see OperationCompleteListener#onSuccess()
     */
    public static void removeItem(@NonNull Activity activity, @NonNull String fridgeName, @NonNull String itemId, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridges").child(fridgeName).child(itemId).removeValue()
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Update an item in the fridge
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Fridge to update the item in
     * @param itemId id of the item to update
     * @param item Item to update the db with
     * @param operationCompleteListener Listener that will be notified when operation completes
     * @see OperationCompleteListener#onSuccess()
     */
    public static void updateItem(@NonNull Activity activity, @NonNull String fridgeName, @NonNull String itemId, @NonNull Item item, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        DatabaseReference db = dbc.getDatabase();
        db.child("fridges").child(fridgeName).child(itemId).setValue(item)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Store name of current user in db
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param name Name to store for the user
     * @param operationCompleteListener Listener that will be notified when operation completes
     * @see OperationCompleteListener#onSuccess()
     */
    public static void setName(@NonNull Activity activity, @NonNull String name, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("user").child(dbc.getUid()).child("name").setValue(name)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    /**
     * Get name of user from the db
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param userId id of the user who's name to retrieve
     * @param userNameListener Listener to receive the retrieved user name along with the user id
     * @see UserNameListener#onResult(String, String)
     */
    public static void getUserName(@NonNull Activity activity, @NonNull String userId, @NonNull UserNameListener userNameListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("user").child(userId).child("name").get().addOnSuccessListener(activity, taskResult -> {
            String result = taskResult.getValue(String.class);
            if (result == null || result.equals("")) {
                result = userId;
            }
            userNameListener.onResult(result, userId);
        }).addOnCanceledListener(activity, userNameListener::onCanceled).addOnFailureListener(activity, userNameListener::onFailure);
    }

    /**
     * Get a list of users who have access to a fridge
     * @param activity Activity whose lifecycle to attach the listeners to. If the activity is stopped the listeners will be automatically removed from the underlying task.
     *                 Leaving the calling activity safe to reference Views in the callbacks without risk of activity leaks
     * @param fridgeName Name of fridge to get users of
     * @param userListListener Listener to receive the list of users from the db along with their user ids
     * @see UserListListener#onResult(Map)
     */
    public static void getUsersInFridge(@NonNull Activity activity, @NonNull String fridgeName, @NonNull UserListListener userListListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").child(fridgeName).get()
                .continueWithTask(new UserListContinuation())
                .addOnSuccessListener(activity, userListListener::onResult)
                .addOnCanceledListener(activity, userListListener::onCanceled)
                .addOnFailureListener(activity, userListListener::onFailure);
    }

}
