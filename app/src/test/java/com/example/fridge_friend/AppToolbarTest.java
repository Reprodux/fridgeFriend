package com.example.fridge_friend;

import com.example.fridge_friend.mocks.MockMenu;
import com.example.fridge_friend.mocks.MockMenuItem;
import com.example.fridge_friend.toolbar.ToolBarInterface;

import junit.framework.TestCase;


public class AppToolbarTest extends TestCase {

    private final MockMenu mockMenu = new MockMenu();
    private final MockMenuItem mockMenuItem = new MockMenuItem();
    private final ToolBarInterface testToolbar = new ToolBarInterface() {};

    @Override
    protected void setUp() {
        mockMenu.reset();
        mockMenuItem.reset();
    }

    public void testAddMenuItems() {
        testToolbar.addMenuItems(mockMenu);
        assertTrue(mockMenu.toHaveBeenCalled("hasVisibleItems"));
    }

    public void testHandleMenuItemSelected() {
        boolean res = testToolbar.handleMenuItemSelected(mockMenuItem);
        assertTrue(mockMenuItem.toHaveBeenCalled("getItemId"));
        assertFalse(res);
    }
}