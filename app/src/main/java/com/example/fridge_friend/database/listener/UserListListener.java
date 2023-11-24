package com.example.fridge_friend.database.listener;

import java.util.Map;

public interface UserListListener extends BaseListener {

    void onResult(Map<String, String> usersInFridge);

}
