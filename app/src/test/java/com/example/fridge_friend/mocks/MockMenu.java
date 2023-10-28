package com.example.fridge_friend.mocks;

import android.content.ComponentName;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import tech.ericw.java.mocks.Mock;

public class MockMenu extends Mock implements Menu {
    @Override
    public MenuItem add(CharSequence title) {
        addCall("add", title);
        return null;
    }

    @Override
    public MenuItem add(int titleRes) {
        addCall("add", titleRes);
        return null;
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        addCall("add", groupId,itemId, order, title);
        return null;
    }

    @Override
    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        addCall("add", groupId, itemId, order, titleRes);
        return null;
    }

    @Override
    public SubMenu addSubMenu(CharSequence title) {
        addCall("addSubMenu", title);
        return null;
    }

    @Override
    public SubMenu addSubMenu(int titleRes) {
        addCall("addSubMenu", titleRes);
        return null;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        addCall("addSubMenu", groupId, itemId, order, title);
        return null;
    }

    @Override
    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        addCall("addSubMenu", groupId, itemId, order, titleRes);
        return null;
    }

    @Override
    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        addCall("addIntentOptions", groupId, itemId, order, caller, specifics, intent, flags, outSpecificItems);
        return 0;
    }

    @Override
    public void removeItem(int id) {
        addCall("removeItem", id);
    }

    @Override
    public void removeGroup(int groupId) {
        addCall("removeGroup", groupId);
    }

    @Override
    public void clear() {
        addCall("clear");
    }

    @Override
    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
        addCall("setGroupCheckable", group, checkable, exclusive);
    }

    @Override
    public void setGroupVisible(int group, boolean visible) {
        addCall("setGroupVisible", group, visible);
    }

    @Override
    public void setGroupEnabled(int group, boolean enabled) {
        addCall("setGroupEnabled", group, enabled);
    }

    @Override
    public boolean hasVisibleItems() {
        addCall("hasVisibleItems");
        return toHaveBeenCalled("add");
    }

    @Override
    public MenuItem findItem(int id) {
        addCall("findItem", id);
        return null;
    }

    @Override
    public int size() {
        addCall("size");
        return 0;
    }

    @Override
    public MenuItem getItem(int index) {
        addCall("getItem", index);
        return null;
    }

    @Override
    public void close() {
        addCall("close");
    }

    @Override
    public boolean performShortcut(int keyCode, KeyEvent event, int flags) {
        addCall("performShortcut", keyCode, event, flags);
        return false;
    }

    @Override
    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        addCall("isShortcutKey", keyCode, event);
        return false;
    }

    @Override
    public boolean performIdentifierAction(int id, int flags) {
        addCall("performIdentifierAction", id, flags);
        return false;
    }

    @Override
    public void setQwertyMode(boolean isQwerty) {
        addCall("setQwertyMode", isQwerty);
    }
}
