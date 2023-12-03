package com.example.fridge_friend.database.listener;

import com.example.fridge_friend.database.Item;

import java.util.Map;

/**
 * Listener for retrieving a list of items from the db
 */
public interface ItemListener extends BaseListener {

    /**
     * Called when the list of items is available from the db
     *
     * @param result Map with item ids as keys and Items as values
     */
    void onResult(Map<String, Item> result);

}
