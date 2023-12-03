package com.example.fridge_friend;

/**
 * The type Fridge item.
 */
public class FridgeItem {
    private String id;
    private String name;
    private String details;

    /**
     * Instantiates a new Fridge item.
     *
     * @param id      the id
     * @param name    the name
     * @param details the details
     */
    public FridgeItem(String id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
// Getters and Setters
    public String getId() { return id; }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() { return name; }

    /**
     * Gets details.
     *
     * @return the details
     */
    public String getDetails() { return details; }
}

