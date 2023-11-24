package com.example.fridge_friend.database.listener;

import com.example.fridge_friend.database.Item;

import java.util.Map;

public interface ItemListener extends BaseListener {

    void onResult(Map<String, Item> result);

}
