package com.example.fridge_friend.mocks;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tech.ericw.java.mocks.Mock;

public class MockMenuItem extends Mock implements MenuItem {
    @Override
    public int getItemId() {
        addCall("getItemId");
        return 0;
    }

    @Override
    public int getGroupId() {
        addCall("getGroupId");
        return 0;
    }

    @Override
    public int getOrder() {
        addCall("getOrder");
        return 0;
    }

    @NonNull
    @Override
    public MenuItem setTitle(@Nullable CharSequence title) {
        addCall("setTitle", title);
        return this;
    }

    @NonNull
    @Override
    public MenuItem setTitle(int title) {
        addCall("setTitle", title);
        return this;
    }

    @Nullable
    @Override
    public CharSequence getTitle() {
        addCall("getTitle");
        return "Mock Title";
    }

    @NonNull
    @Override
    public MenuItem setTitleCondensed(@Nullable CharSequence title) {
        addCall("setTitleCondensed", title);
        return this;
    }

    @Nullable
    @Override
    public CharSequence getTitleCondensed() {
        addCall("getTitleCondensed");
        return "Mock Condensed Title";
    }

    @NonNull
    @Override
    public MenuItem setIcon(@Nullable Drawable icon) {
        addCall("setIcon", icon);
        return this;
    }

    @NonNull
    @Override
    public MenuItem setIcon(int iconRes) {
        addCall("setIcon", iconRes);
        return this;
    }

    @Nullable
    @Override
    public Drawable getIcon() {
        addCall("getIcon");
        return null;
    }

    @NonNull
    @Override
    public MenuItem setIntent(@Nullable Intent intent) {
        addCall("setIntent", intent);
        return this;
    }

    @Nullable
    @Override
    public Intent getIntent() {
        addCall("getIntent");
        return null;
    }

    @NonNull
    @Override
    public MenuItem setShortcut(char numericChar, char alphaChar) {
        addCall("setShortcut", numericChar, alphaChar);
        return this;
    }

    @NonNull
    @Override
    public MenuItem setNumericShortcut(char numericChar) {
        addCall("setNumericShortcut", numericChar);
        return this;
    }

    @Override
    public char getNumericShortcut() {
        addCall("getNumericShortcut");
        return 0;
    }

    @NonNull
    @Override
    public MenuItem setAlphabeticShortcut(char alphaChar) {
        addCall("setAlphabeticShortcut", alphaChar);
        return this;
    }

    @Override
    public char getAlphabeticShortcut() {
        addCall("getAlphabeticShortcut");
        return 0;
    }

    @NonNull
    @Override
    public MenuItem setCheckable(boolean checkable) {
        addCall("setCheckable", checkable);
        return this;
    }

    @Override
    public boolean isCheckable() {
        addCall("isCheckable");
        return lastCalledWith("setCheckable", true);
    }

    @NonNull
    @Override
    public MenuItem setChecked(boolean checked) {
        addCall("setChecked", checked);
        return this;
    }

    @Override
    public boolean isChecked() {
        addCall("isChecked");
        return lastCalledWith("setChecked", true);
    }

    @NonNull
    @Override
    public MenuItem setVisible(boolean visible) {
        addCall("setVisible", visible);
        return this;
    }

    @Override
    public boolean isVisible() {
        addCall("isVisible");
        return lastCalledWith("setVisible", true);
    }

    @NonNull
    @Override
    public MenuItem setEnabled(boolean enabled) {
        addCall("setEnabled", enabled);
        return this;
    }

    @Override
    public boolean isEnabled() {
        addCall("isEnabled");
        return lastCalledWith("setEnabled", true);
    }

    @Override
    public boolean hasSubMenu() {
        addCall("hasSubMenu");
        return false;
    }

    @Nullable
    @Override
    public SubMenu getSubMenu() {
        addCall("getSubMenu");
        return null;
    }

    @NonNull
    @Override
    public MenuItem setOnMenuItemClickListener(@Nullable OnMenuItemClickListener menuItemClickListener) {
        addCall("setOnMenuItemClickListener", menuItemClickListener);
        return this;
    }

    @Nullable
    @Override
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        addCall("getMenuInfo");
        return null;
    }

    @Override
    public void setShowAsAction(int actionEnum) {
        addCall("setShowAsAction", actionEnum);
    }

    @NonNull
    @Override
    public MenuItem setShowAsActionFlags(int actionEnum) {
        addCall("setShowAsActionFlags", actionEnum);
        return this;
    }

    @NonNull
    @Override
    public MenuItem setActionView(@Nullable View view) {
        addCall("setActionView", view);
        return this;
    }

    @NonNull
    @Override
    public MenuItem setActionView(int resId) {
        addCall("setActionView", resId);
        return this;
    }

    @Nullable
    @Override
    public View getActionView() {
        addCall("getActionView");
        return null;
    }

    @NonNull
    @Override
    public MenuItem setActionProvider(@Nullable ActionProvider actionProvider) {
        addCall("setActionProvider", actionProvider);
        return null;
    }

    @Nullable
    @Override
    public ActionProvider getActionProvider() {
        addCall("getActionProvider");
        return null;
    }

    @Override
    public boolean expandActionView() {
        addCall("expandActionView");
        return false;
    }

    @Override
    public boolean collapseActionView() {
        addCall("collapseActionView");
        return false;
    }

    @Override
    public boolean isActionViewExpanded() {
        addCall("isActionViewExpanded");
        return false;
    }

    @NonNull
    @Override
    public MenuItem setOnActionExpandListener(@Nullable OnActionExpandListener listener) {
        addCall("setOnActionExpandListener", listener);
        return this;
    }
}
