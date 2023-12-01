package com.example.fridge_friend.database.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for connecting to local db
 */
public class CartDatabase {

    /**
     * Get a list of items currently in the shopping cart from the db
     * @param context Context with access to the db
     * @return List of shopping cart items
     */
    public static List<ShoppingCartItem> getItems(@NonNull Context context) {
        List<ShoppingCartItem> items = new ArrayList<>();
        try (CartDatabaseHelper helper = new CartDatabaseHelper(context)) {
            try (SQLiteDatabase db = helper.getReadableDatabase()) {
                String query = "SELECT * FROM  " + CartDatabaseHelper.TABLE_NAME;
                Cursor cursor = db.rawQuery(query,null);
                while (cursor.moveToNext()){
                    int idIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_ITEM_NAME);
                    int quantityIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_QUANTITY);
                    int checkedIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_ITEM_CHECKED);
                    int upcIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_UPC);
                    long id = cursor.getLong(idIndex);
                    String name = cursor.getString(nameIndex);
                    int quantity = cursor.getInt(quantityIndex);
                    boolean checked = cursor.getInt(checkedIndex) == 1;
                    String upc = cursor.getString(upcIndex);
                    ShoppingCartItem item = new ShoppingCartItem(id, name, quantity, checked,upc);
                    items.add(item);
                }
            }
        }


        return items;
    }

    /**
     * Get an item currently in the shopping cart from the db
     * @param context Context with access to the db
     * @param itemId Id of item to retrieve
     * @return Shopping cart item or null if the item doesn't exist in the db
     * FOR SOME REASON IT DOESNT WORK YOU NEED TO USE A RAW QUERY INSTEAD
     */
    @Nullable
    public static ShoppingCartItem getItem(@NonNull Context context, long itemId) {
        try (CartDatabaseHelper helper = new CartDatabaseHelper(context)) {
            try (SQLiteDatabase db = helper.getReadableDatabase()) {
                {
                    String query = "SELECT * FROM  " + CartDatabaseHelper.TABLE_NAME + " WHERE " + CartDatabaseHelper.KEY_ID + " = " + String.valueOf(itemId);
                    Cursor cursor = db.rawQuery(query,null);
                    while (cursor.moveToNext()) {
                        int idIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_ID);
                        int nameIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_ITEM_NAME);
                        int quantityIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_QUANTITY);
                        int checkedIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_ITEM_CHECKED);
                        int upcIndex = cursor.getColumnIndexOrThrow(CartDatabaseHelper.KEY_UPC);
                        long id = cursor.getLong(idIndex);
                        String name = cursor.getString(nameIndex);
                        int quantity = cursor.getInt(quantityIndex);
                        boolean checked = cursor.getInt(checkedIndex) == 1;
                        String upc = cursor.getString(upcIndex);
                        ShoppingCartItem item = new ShoppingCartItem(id, name, quantity, checked, upc);
                        return item;
                    }}}}

        return null;
    }

    /**
     * Store (insert or update) a shopping cart item in the db.
     * item is updated if {@link ShoppingCartItem#getId()} > 0.
     * If {@link ShoppingCartItem#getId()} = 0 then insert is performed instead and the key is updated
     * @param context Context with access to the db
     * @param item item to store
     * @return true if the store was successful, false otherwise
     */
    public static boolean storeItem(@NonNull Context context, @NonNull ShoppingCartItem item) {
        try (CartDatabaseHelper helper = new CartDatabaseHelper(context)) {
            try (SQLiteDatabase db = helper.getWritableDatabase()) {
                if (item.getId() > 0) {
                    int rowsChanged = db.update(CartDatabaseHelper.TABLE_NAME, item.getContentValues(), CartDatabaseHelper.KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
                    return rowsChanged > 0;
                } else {
                    long id = db.insert(CartDatabaseHelper.TABLE_NAME, null, item.getContentValues());
                    if (id > 0) {
                        item.setId(id);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Store (insert or update) items in a shopping cart in the db.
     * item is updated if {@link ShoppingCartItem#getId()} > 0.
     * If {@link ShoppingCartItem#getId()} = 0 then insert is performed instead and the key is updated
     * @param context Context with access to the db
     * @param items list of items to store
     * @return true if the store was successful, false otherwise
     */
    public static boolean storeItems(@NonNull Context context, @NonNull List<ShoppingCartItem> items) {
        boolean result = true;
        try (CartDatabaseHelper helper = new CartDatabaseHelper(context)) {
            try (SQLiteDatabase db = helper.getWritableDatabase()) {
                for (ShoppingCartItem item: items) {
                    if (item.getId() > 0) {
                        int rowsChanged = db.update(CartDatabaseHelper.TABLE_NAME, item.getContentValues(), CartDatabaseHelper.KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
                        result = result && rowsChanged > 0;
                    } else {
                        long id = db.insert(CartDatabaseHelper.TABLE_NAME, null, item.getContentValues());
                        if (id > 0) {
                            item.setId(id);
                        }
                        result = result && id > 0;
                    }
                }
            }
        }
        return result && items.size() > 0;
    }

    /**
     * Remove an item from the db
     * @param context Context with access to the db
     * @param itemId Id (key) of item to delete
     * @return true if delete was successful, false otherwise
     */
    public static boolean deleteItem(@NonNull Context context, long itemId) {
        if (itemId <= 0) return false;
        try (CartDatabaseHelper helper = new CartDatabaseHelper(context)) {
            try (SQLiteDatabase db = helper.getWritableDatabase()) {
                int rowsChanged = db.delete(CartDatabaseHelper.TABLE_NAME, CartDatabaseHelper.KEY_ID + " = ?", new String[]{String.valueOf(itemId)});
                return rowsChanged > 0;
            }
        }
    }

    /**
     * Remove all items from the db
     * @param context Context with access to the db
     * @return true if delete was successful, false otherwise
     */
    public static boolean clearCart(@NonNull Context context) {
        try (CartDatabaseHelper helper = new CartDatabaseHelper(context)) {
            try (SQLiteDatabase db = helper.getWritableDatabase()) {
                int rowsChanged = db.delete(CartDatabaseHelper.TABLE_NAME, "1", null);
                return rowsChanged > 0;
            }
        }
    }

}
