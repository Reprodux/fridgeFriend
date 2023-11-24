package com.example.fridge_friend.database.listener;

import java.util.Map;

/**
 * Listener for retrieving a list of users from the db
 */
public interface UserListListener extends BaseListener {

    /**
     * Called when the list of users is available from the db
     * @param usersInFridge Map with user ids as the keys and names as values
     */
    void onResult(Map<String, String> usersInFridge);

}
