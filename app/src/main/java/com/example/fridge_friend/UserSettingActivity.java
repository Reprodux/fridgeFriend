package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.database.listener.UserNameListener;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type User setting activity.
 *
 * @noinspection deprecation
 */
public class UserSettingActivity extends AppToolbar {

    private LinearLayout layoutPersonalSettingsInfo;
    private LinearLayout layoutNotificationSettings;
    private SwitchCompat switchAllowNotifications;
    private ProgressDialog progressPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            finish();
            return;
        }

        // Initializing the hidden layout for personal settings
        layoutPersonalSettingsInfo = findViewById(R.id.layoutPersonalSettingsInfo);
        // Initialize the hidden layout for notifications
        layoutNotificationSettings = findViewById(R.id.layoutNotificationSettings);
        switchAllowNotifications = findViewById(R.id.switchAllowNotifications);
        //Popup for progress bar substitute
        progressPopup = new ProgressDialog(this);
        progressPopup.setMessage(getString(R.string.please_wait));
        progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressPopup.setMax(100);
        progressPopup.setCancelable(false);
        progressPopup.show();
        Database.getUserName(this, uid, new LoadingListener(this));
    }

    /**
     * Toggle personal settings.
     *
     * @param view the view
     */
// toggling the Personal Settings section
    public void togglePersonalSettings(View view) {
        // Toggling the visibility of the expandable layout
        layoutPersonalSettingsInfo.setVisibility(
                layoutPersonalSettingsInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
        );
        if (layoutPersonalSettingsInfo.getVisibility() == View.VISIBLE) {
            Button saveButton = layoutPersonalSettingsInfo.findViewById(R.id.buttonSavePersonalSettings);
            saveButton.setOnClickListener(v -> {
                EditText nameText = layoutPersonalSettingsInfo.findViewById(R.id.editTextName);
                if (nameText != null) {
                    String newName = nameText.getEditableText().toString();
                    if (newName.trim().length() == 0) {
                        nameText.setError(getString(R.string.enter_name));
                    } else {
                        nameText.setError(null);
                        progressPopup.setMessage(getString(R.string.saving_changes));
                        progressPopup.show();
                        Database.setName(this, newName, new SavingListener(this));
                    }
                }
            });
        }
    }

    /**
     * On checked changed.
     *
     * @param buttonView the button view
     * @param isChecked  the is checked
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Handle switch toggle
        if (isChecked) {
            // Code to handle notifications allowed
        } else {
            // Code to handle notifications disallowed
        }
    }

    /**
     * Toggle notification settings.
     *
     * @param view the view
     */
// Method to toggle the Notification Settings section
    public void toggleNotificationSettings(View view) {
        layoutNotificationSettings.setVisibility(
                layoutNotificationSettings.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
        );
    }

    private class SavingListener implements OperationCompleteListener {

        private final UserSettingActivity activity;

        /**
         * Instantiates a new Saving listener.
         *
         * @param activity the activity
         */
        SavingListener(UserSettingActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Toast.makeText(activity, R.string.changes_saved, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("UserSettings", "Saving Error!", e);
            activity.progressPopup.dismiss();
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_saving_changes);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

    }

    private class LoadingListener implements UserNameListener {

        private final UserSettingActivity activity;

        /**
         * Instantiates a new Loading listener.
         *
         * @param activity the activity
         */
        LoadingListener(UserSettingActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onResult(String name, String id) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            EditText nameText = activity.layoutPersonalSettingsInfo.findViewById(R.id.editTextName);
            if (nameText != null && id.equals(FirebaseAuth.getInstance().getUid())) {
                // Double check that we've received the name of our user
                nameText.setText(name);
            }
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("UserSettings", "Loading Error!", e);
            activity.progressPopup.dismiss();
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_loading_name);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

    }
    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((UserSettingActivity.this));
        alert_builder.setTitle(R.string.user_settings_title).setMessage(R.string.userSettingsAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");
        }).show();
    }
}
