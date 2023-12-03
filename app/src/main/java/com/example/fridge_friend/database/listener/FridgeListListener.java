package com.example.fridge_friend.database.listener;

import java.util.List;

/**
 * Listener for retrieving a list of fridges from the db
 */
public interface FridgeListListener extends BaseListener {

    /**
     * Called when the list of fridges is available from the db
     *
     * @param result List of fridge names
     */
    void onListResult(List<String> result);

}
