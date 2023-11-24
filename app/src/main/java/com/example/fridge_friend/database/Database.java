package com.example.fridge_friend.database;

import android.app.Activity;

import androidx.annotation.NonNull;

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

public class Database {
    public static void listFridges(Activity activity, @NonNull FridgeListListener fridgeListListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("userAccess").child(dbc.getUid()).get().addOnSuccessListener(activity, taskResult -> {
            List<String> fridgeNames = new ArrayList<>();
            for(DataSnapshot snapshot: taskResult.getChildren()) {
                fridgeNames.add(snapshot.getKey());
            }
            fridgeListListener.onListResult(fridgeNames);
        }).addOnCanceledListener(activity, fridgeListListener::onCanceled).addOnFailureListener(activity, fridgeListListener::onFailure);
    }

    public static void getAPIResult(Activity activity, String upc, @NonNull APIResultListener apiResultListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("apiCache").child(upc).get().addOnSuccessListener(activity, taskResult -> {
            String result = taskResult.getValue(String.class);
            apiResultListener.onResult(result);
        }).addOnCanceledListener(activity, apiResultListener::onCanceled).addOnFailureListener(activity, apiResultListener::onFailure);
    }

    public static void putAPIResult(Activity activity, String upc, String apiResult, OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        DatabaseReference db = dbc.getDatabase();
        db.child("apiCache").child(upc).setValue(apiResult)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void joinFridge(Activity activity, String fridgeName, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").get()
                .continueWithTask(new JoinFridgeContinuation(fridgeName, dbc.getUid()))
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void newFridge(Activity activity, String fridgeName, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").get()
                .continueWithTask(new CreateFridgeContinuation(fridgeName, dbc.getUid()))
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void leaveFridge(Activity activity, String fridgeName, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").child(fridgeName).get()
                .continueWithTask(new LeaveFridgeContinuation(fridgeName, dbc.getUid()))
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void getItems(Activity activity, String fridgeName, @NonNull ItemListener itemListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridges").child(fridgeName).get().addOnSuccessListener(activity, taskResult -> {
            Map<String, Item> result = taskResult.getValue(new GenericTypeIndicator<Map<String, Item>>() {});
            itemListener.onResult(result);
        }).addOnCanceledListener(activity, itemListener::onCanceled).addOnFailureListener(activity, itemListener::onFailure);
    }

    public static void addItem(Activity activity, String fridgeName, Item item, OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        DatabaseReference db = dbc.getDatabase();
        db.child("fridges").child(fridgeName).push().setValue(item)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void removeItem(Activity activity, String fridgeName, String itemId, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridges").child(fridgeName).child(itemId).removeValue()
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void updateItem(Activity activity, String fridgeName, String itemId, Item item, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        DatabaseReference db = dbc.getDatabase();
        db.child("fridges").child(fridgeName).child(itemId).setValue(item)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void setName(Activity activity, String name, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("users").child(dbc.getUid()).child("name").setValue(name)
                .addOnSuccessListener(activity, unused -> operationCompleteListener.onSuccess())
                .addOnCanceledListener(activity, operationCompleteListener::onCanceled)
                .addOnFailureListener(activity, operationCompleteListener::onFailure);
    }

    public static void getUserName(Activity activity, String userId, @NonNull UserNameListener userNameListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("users").child(userId).child("name").get().addOnSuccessListener(activity, taskResult -> {
            String result = taskResult.getValue(String.class);
            userNameListener.onResult(result);
        }).addOnCanceledListener(activity, userNameListener::onCanceled).addOnFailureListener(activity, userNameListener::onFailure);
    }

    public static void getUsersInFridge(Activity activity, String fridgeName, UserListListener userListListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("fridgeMembers").child(fridgeName).get()
                .continueWithTask(new UserListContinuation())
                .addOnSuccessListener(activity, userListListener::onResult)
                .addOnCanceledListener(activity, userListListener::onCanceled)
                .addOnFailureListener(activity, userListListener::onFailure);
    }

}
