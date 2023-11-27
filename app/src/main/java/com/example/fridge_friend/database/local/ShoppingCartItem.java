package com.example.fridge_friend.database.local;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Item in the local shopping cart db
 * This class is NOT backed by the db meaning any updates here will not be reflected in the db and vice versa
 */
public class ShoppingCartItem {

    private long id;
    private String name;
    private int quantity;
    private boolean checked;

    /**
     * ONLY FOR INTERNAL USE!
     * Create a Shopping cart item from the db
     * @param id db key
     * @param name item name
     * @param quantity item quantity
     * @param checked if the item is checked
     * @see CartDatabase#getItem(Context, long)
     * @see CartDatabase#getItems(Context)
     */
    ShoppingCartItem(long id, @NonNull String name, int quantity, boolean checked) {
        this.id = id;
        this.name = name;
        this.quantity = Math.max(quantity, 1);
        this.checked = checked;
    }

    /**
     * Create new Shopping cart item
     * @param name item name
     * @param amount item quantity
     */
    public ShoppingCartItem(@NonNull String name, int amount) {
        new ShoppingCartItem(0, name, amount, false);
    }

    /**
     * ONLY FOR INTERNAL USE!
     * Set the db key of the item
     * @param id db key
     */
    void setId(long id) {
        this.id = id;
    }

    /**
     * ONLY FOR INTERNAL USE!
     * Get the db key
     * @return db key, 0 if new > 0 if existing in db
     */
    long getId() {
        return id;
    }

    /**
     * Get item name
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * Get item quantity
     * @return item quantity > 0
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get if the item is checked or not
     * @return true if checked, false if not
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Set quantity of item
     * NOTE: This does not update the stored item in the db, it must be updated separately
     * @param quantity quantity to set >= 1
     * @see CartDatabase#storeItem(Context, ShoppingCartItem) 
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(quantity, 1);
    }

    /**
     * Check the item in the cart
     * NOTE: This does not update the stored item in the db, it must be updated separately
     * @see CartDatabase#storeItem(Context, ShoppingCartItem) 
     */
    public void check() {
        this.checked = true;
    }

    /**
     * Uncheck the item in the cart
     * NOTE: This does not update the stored item in the db, it must be updated separately
     * @see CartDatabase#storeItem(Context, ShoppingCartItem) 
     */
    public void uncheck() {
        this.checked = false;
    }

    /**
     * ONLY FOR INTERNAL USE!
     * Convert to ContentValues for saving
     * @return ContentValues that represent the Shopping cart item
     * @see CartDatabase#storeItem(Context, ShoppingCartItem)
     */
    ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(CartDatabaseHelper.KEY_ITEM_NAME, name);
        values.put(CartDatabaseHelper.KEY_QUANTITY, quantity);
        values.put(CartDatabaseHelper.KEY_ITEM_CHECKED, checked);
        return values;
    }
}
