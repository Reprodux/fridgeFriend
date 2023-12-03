package com.example.fridge_friend.database.errors;

/**
 * Error thrown when creating a new fridge if the given fridge name is already in use
 */
public class FridgeInUseException extends Exception{

    /**
     * Instantiates a new Fridge in use exception.
     *
     * @param s the s
     */
    public FridgeInUseException(String s) {
        super(s);
    }

}
