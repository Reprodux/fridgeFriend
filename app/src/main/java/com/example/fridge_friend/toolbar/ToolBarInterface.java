package com.example.fridge_friend.toolbar;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

/**
 * The interface Tool bar interface.
 */
public interface ToolBarInterface {

    /**
     * Handle when the user selects an item that was added to the menu with addMenuItems(Menu menu)
     *
     * @param item - Menu item selected
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here
     */
    default boolean handleMenuItemSelected(@NonNull MenuItem item) {
        // Override in subclass to handle additional menu item selections
        item.getItemId();
        return false;
    }

    /**
     * Add additional items to the app toolbar
     *
     * @param menu - Menu to add items to
     */
    default void addMenuItems(Menu menu) {
        // Override in subclass to add additional items
        menu.hasVisibleItems();
    }

}