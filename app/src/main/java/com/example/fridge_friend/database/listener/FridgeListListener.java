package com.example.fridge_friend.database.listener;

import java.util.List;

public interface FridgeListListener extends BaseListener {

    void onListResult(List<String> result);

}
