package com.example.fridge_friend.database.errors;
public class FridgeInUseException extends Exception{

    public FridgeInUseException(String s) {
        super(s);
    }

}
