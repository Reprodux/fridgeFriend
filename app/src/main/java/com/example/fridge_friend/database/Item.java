package com.example.fridge_friend.database;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Item {
    private String name;
    private Long amount = 1L;
    private String expiry = "";
    private String owner = null;

    public Item() {}

    public Item(String name) {
        new Item(name, 1L, "", null);
    }

    public Item(String name, Long amount) {
        new Item(name, amount, "", null);
    }

    public Item(String name, Long amount, String expiry) {
        new Item(name, amount, expiry, null);
    }

    public Item(String name, Long amount, String expiry, String owner) {
        this.name = name;
        this.amount = amount;
        this.expiry = expiry;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public Long getAmount() {
        return amount;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getOwner() { return owner; }

    @Exclude
    public void incrementAmount() {
        incrementAmount(1);
    }

    @Exclude
    public void incrementAmount(int count) {
        if (count > 0) {
            amount += count;
        }
    }

    @Exclude
    public void decrementAmount() {
        decrementAmount(1);
    }

    @Exclude
    public void decrementAmount(int count) {
        if (count > 0 && amount - count >= 0) {
            amount -= count;
        }
    }

    @Exclude
    public void setExpiry(String expiry) {
        if (expiry != null) {
            this.expiry = expiry;
        } else {
            this.expiry = "";
        }
    }

    @Exclude
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
