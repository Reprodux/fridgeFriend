package com.example.fridge_friend;

public class FridgeItem {
    private String id;
    private String name;
    private String details;

    public FridgeItem(String id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDetails() { return details; }
}

