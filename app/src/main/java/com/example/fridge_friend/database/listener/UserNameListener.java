package com.example.fridge_friend.database.listener;

/**
 * Listener for retrieving the name of a user from the db
 */
public interface UserNameListener extends BaseListener {

    /**
     * Called when the name of a user is available from the db
     *
     * @param name name of the user retrieved from the db
     * @param id   id of the user that the name is connected to
     */
    void onResult(String name, String id);

}
