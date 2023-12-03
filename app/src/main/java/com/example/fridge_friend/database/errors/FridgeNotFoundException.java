package com.example.fridge_friend.database.errors;

/**
 * Error thrown when the requested fridge to join isn't found
 */
public class FridgeNotFoundException extends Exception {
    /**
     * Instantiates a new Fridge not found exception.
     *
     * @param s the s
     */
    public FridgeNotFoundException(String s) {
        super(s);
    }

}
