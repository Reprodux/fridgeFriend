package com.example.fridge_friend;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class UserSettingActivity extends AppCompatActivity {

    private LinearLayout layoutPersonalSettingsInfo;
    private LinearLayout layoutNotificationSettings;
    private Switch switchAllowNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        // Initializing the hidden layout for personal settings
        layoutPersonalSettingsInfo = findViewById(R.id.layoutPersonalSettingsInfo);
        // Initialize the hidden layout for notifications
        layoutNotificationSettings = findViewById(R.id.layoutNotificationSettings);
        switchAllowNotifications = findViewById(R.id.switchAllowNotifications);
    }

    // toggling the Personal Settings section
    public void togglePersonalSettings(View view) {
        // Toggling the visibility of the expandable layout
        layoutPersonalSettingsInfo.setVisibility(
                layoutPersonalSettingsInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
        );
    }
    // toggling for the Notification Settings section
    public void toggleNotifications(View view) {
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
    // Method to toggle the Notification Settings section
    public void toggleNotificationSettings(View view) {
        layoutNotificationSettings.setVisibility(
                layoutNotificationSettings.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
        );
    }

}
