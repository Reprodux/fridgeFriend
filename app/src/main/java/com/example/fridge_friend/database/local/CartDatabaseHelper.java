package com.example.fridge_friend.database.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

/**
 * Helper for managing the actual connection and state of the local SQLite database
 */
public class CartDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ShoppingCart.db";
    private static final int VERSION_NUM = 4;
    /**
     * The Key id.
     */
    static final String KEY_ID = "id";
    /**
     * The Key item name.
     */
    static final String KEY_ITEM_NAME = "name";
    /**
     * The Key item checked.
     */
    static final String KEY_ITEM_CHECKED = "checked";
    /**
     * The Key quantity.
     */
    static final String KEY_QUANTITY = "quantity";
    /**
     * The Key upc.
     */
    static final String KEY_UPC = "upc";
    /**
     * The Table name.
     */
    static final String TABLE_NAME = "cart";

    /**
     * Instantiates a new Cart database helper.
     *
     * @param context the context
     */
    public CartDatabaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("+ KEY_ID +" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+ KEY_ITEM_NAME +" TEXT NOT NULL DEFAULT '', "+ KEY_UPC +" TEXT NOT NULL DEFAULT '', "
                + KEY_QUANTITY +" INTEGER NOT NULL DEFAULT 1 CHECK("+ KEY_QUANTITY +" >= 1), "
                + KEY_ITEM_CHECKED+" BOOLEAN NOT NULL DEFAULT 0 CHECK ("+ KEY_ITEM_CHECKED +" IN (0, 1)))");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
