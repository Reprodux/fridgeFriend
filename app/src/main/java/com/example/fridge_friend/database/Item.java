package com.example.fridge_friend.database;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Item in the fridge.
 * This class is NOT backed by the db meaning any updates here will not be reflected in the db and vice versa
 */
@IgnoreExtraProperties
public class Item {
    private String name;
    private Long amount = 1L;
    private String expiry = "";
    private String owner = null;

    /**
     * NOT FOR NORMAL USAGE.
     * Required for Firebase to be able to deserialize Items from the db
     * @deprecated
     */
    public Item() {}

    public Item(String name) {
        this(name, 1L, "", null);
    }

    public Item(String name, Long amount) {
        this(name, amount, "", null);
    }

    public Item(String name, Long amount, String expiry) {
        this(name, amount, expiry, null);
    }

    public Item(String name, Long amount, String expiry, String owner) {
        this.name = name;
        this.amount = amount;
        this.expiry = expiry;
        this.owner = owner;
    }

    /**
     * Get the name of the item
     * @return name of item
     */
    public String getName() {
        return name;
    }

    /**
     * Get the amount of item in fridge
     * @return amount >= 0
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Get the items expiry date
     * @return Items expiry date or empty string if none stored
     */
    public String getExpiry() {
        return expiry;
    }

    /**
     * Get the owner of the item.
     * Name of owner must be fetched separately
     * @return id of the item owner or null if there is no owner
     */
    public String getOwner() { return owner; }

    /**
     * Increase the amount of items in the fridge by 1.
     * NOTE: This does not update the stored item in the db, it must be updated separately
     */
    @Exclude
    public void incrementAmount() {
        incrementAmount(1);
    }

    /**
     * Increase the amount of items in the fridge by a positive number.
     * NOTE: This does not update the stored item in the db, it must be updated separately
     * @param amount amount to increase by
     */
    @Exclude
    public void incrementAmount(int amount) {
        if (amount > 0) {
            this.amount += amount;
        }
    }

    /**
     * Decrease the amount of items in the fridge by 1.
     * NOTE: This does not update the stored item in the db, it must be updated separately
     */
    @Exclude
    public void decrementAmount() {
        decrementAmount(1);
    }

    /**
     * Decrease the amount of items in the fridge by a positive number.
     * NOTE: This does not update the stored item in the db, it must be updated separately
     * @param amount amount to decrease by
     */
    @Exclude
    public void decrementAmount(int amount) {
        if (amount > 0 && this.amount - amount >= 0) {
            this.amount -= amount;
        }
    }

    /**
     * Set the expiry date of the item.
     * NOTE: This does not update the stored item in the db, it must be updated separately
     * @param expiry date string to set as the expiry
     */
    @Exclude
    public void setExpiry(String expiry) {
        if (expiry != null) {
            this.expiry = expiry;
        } else {
            this.expiry = "";
        }
    }

    /**
     * Set the owner of the item.
     * NOTE: This does not update the stored item in the db, it must be updated separately.
     * NOTE 2: The db will reject updates if the owner of the item doesn't have access to the fridge
     * @param owner owner id to set as owner
     */
    @Exclude
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
