package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.fridge_friend.toolbar.AppToolbar;

public class FridgeSettingActivity extends AppToolbar {

    private LinearLayout layoutPersonalSettingsInfo;
    private LinearLayout layoutNotificationSettings;
    private Switch switchAllowNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_creation);

        // Initializing the hidden layout for personal settings
        layoutPersonalSettingsInfo = findViewById(R.id.layoutNewFridgeInfo);
        // Initialize the hidden layout for notifications
        layoutNotificationSettings = findViewById(R.id.layoutJoinExistingFridgeInput);

    }

    // toggling the Personal Settings section
    public void toggleNewFridge(View view) {
        // Toggling the visibility of the expandable layout
        layoutPersonalSettingsInfo.setVisibility(
                layoutPersonalSettingsInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
        );
    }
    // toggling for the Notification Settings section
    public void toggleJoinFridge(View view) {
        // Toggle the visibility of the notification settings layout
        layoutNotificationSettings.setVisibility(
                layoutNotificationSettings.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
        );
    }
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Handle switch toggle
        if(isChecked) {
            // Code to handle notifications allowed
        } else {
            // Code to handle notifications disallowed
        }
    }
    //Overrides toolbars about to display info on current activity
    @Override
    public void about() {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder((FridgeSettingActivity.this));
        alert_builder.setTitle(R.string.fridge_setting_title).setMessage(R.string.fridgeSettingAbout);
        alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }


}
