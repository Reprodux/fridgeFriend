package com.example.fridge_friend.database;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.fridge_friend.database.listener.APIResultListener;
import com.example.fridge_friend.database.listener.FridgeListListener;
import com.example.fridge_friend.database.listener.FridgeResultListener;
import com.example.fridge_friend.database.listener.ItemListener;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.database.listener.UserListListener;
import com.example.fridge_friend.database.listener.UserNameListener;
import com.example.fridge_friend.database.util.DatabaseConnection;
import com.example.fridge_friend.database.util.UserListContinuation;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.List;
import java.util.Map;

public class Database {
    public static void listFridges(Activity activity, @NonNull FridgeListListener fridgeListListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("userAccess").child(dbc.getUid()).get().addOnSuccessListener(activity, taskResult -> {
            List<String> fridgeNames = taskResult.getValue(new GenericTypeIndicator<List<String>>() {});
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

    public static void joinFridge(Activity activity, String fridgeName, OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
    }

    public static void newFridge(Activity activity, String fridgeName, FridgeResultListener fridgeResultListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
    }

    public static void leaveFridge(Activity activity, String fridgeName, OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
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
    }

    public static void removeItem(Activity activity, String fridgeName, String itemId, OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
    }

    public static void updateItem(Activity activity, String fridgeName, String itemId, Item item, OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
    }

    public static void setName(Activity activity, String name, @NonNull OperationCompleteListener operationCompleteListener) {
        DatabaseConnection dbc = DatabaseConnection.getConnection();
        dbc.getDatabase().child("users").child(dbc.getUid()).child("name").setValue(name)
                .addOnSuccessListener(activity, unused -> {
                    operationCompleteListener.onSuccess();
                }).addOnCanceledListener(activity, operationCompleteListener::onCanceled).addOnFailureListener(activity, operationCompleteListener::onFailure);
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
