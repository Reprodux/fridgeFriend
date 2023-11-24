package com.example.fridge_friend.database.listener;

/**
 * Listener for retrieving API results that were previously stored in the db
 */
public interface APIResultListener extends BaseListener {

    /**
     * Called when the API result has been retrieved from the db and is available
     * @param result API result string that was retrieved from the db
     */
    void onResult(String result);

}
