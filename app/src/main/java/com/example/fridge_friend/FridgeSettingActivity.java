package com.example.fridge_friend;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.FridgeListListener;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.toolbar.AppToolbar;

import java.util.List;

/**
 * The type Fridge setting activity.
 */
public class FridgeSettingActivity extends AppToolbar {

    private LinearLayout layoutPersonalSettingsInfo;
    private LinearLayout layoutNotificationSettings;
    private Switch switchAllowNotifications;

    private EditText editTextNewFridgeName;
    private Button buttonCreateNewFridge;

    private EditText autoCompleteFridgeTextView;
    private String fridgeName;
    private ArrayAdapter<String> autoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_creation);

        //initializing views
        editTextNewFridgeName = findViewById(R.id.editTextNewFridgeName);
        buttonCreateNewFridge = findViewById(R.id.buttonCreateNewFridge);


        // Initializing the hidden layout for personal settings
        layoutPersonalSettingsInfo = findViewById(R.id.layoutNewFridgeInfo);
        // Initialize the hidden layout for notifications
        layoutNotificationSettings = findViewById(R.id.layoutJoinExistingFridgeInput);

        //button click listener for creating new fridge
        buttonCreateNewFridge.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                String fridgeName = editTextNewFridgeName.getText().toString().trim();
                if(!fridgeName.isEmpty()){
                    createNewFridge(fridgeName);
                }
                else{
                    Toast.makeText(FridgeSettingActivity.this, "Fridge name cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }


        });
        autoCompleteFridgeTextView = findViewById(R.id.autoCompleteFridgeTextView);
        Button join = findViewById(R.id.buttonRequestJoinFridge);

        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fridgeName = autoCompleteFridgeTextView.getText().toString();
                Database.joinFridge(FridgeSettingActivity.this, fridgeName, new OperationCompleteListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(FridgeSettingActivity.this, "Joined fridge successfully", Toast.LENGTH_SHORT).show();

                        // Redirecting user to FridgeDetailActivity
                        Intent intent = new Intent(FridgeSettingActivity.this, FridgeDetailActivity.class);
                        intent.putExtra("EXTRA_FRIDGE_NAME", fridgeName);
                        startActivity(intent);

                        finish();


                    }

                    @Override
                    public void onCanceled() {
                        // Handling the operation being cancelled
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handling the failure case, such as if the fridge does not exist
                        Toast.makeText(FridgeSettingActivity.this, "Failed to join fridge: " + fridgeName, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // Fetching the list of fridges and set up the adapter
        fetchFridgesAndSetUpAdapter();


    }

    private void createNewFridge(String fridgeName){
        //using the database class to create a new fridge
        Database.newFridge(FridgeSettingActivity.this, fridgeName, new OperationCompleteListener(){
            @Override
            public void onSuccess(){
                Toast.makeText(FridgeSettingActivity.this, "New Fridge created successfully", Toast.LENGTH_SHORT).show();

                //clearing the editText
                editTextNewFridgeName.setText("");
            }

            @Override
            public void onCanceled() {
                //handling operation being cancelled
            }
            @Override
            public void onFailure(@NonNull Exception e) {
                //log error
                Log.e("CreateFridgeError", "Failed to create a new fridge: " + e.getMessage(), e);

                Toast.makeText(FridgeSettingActivity.this, "Fridge with same name may exist: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }

    /**
     * Toggle join fridge.
     *
     * @param 
     */

    private void fetchFridgesAndSetUpAdapter() {
        Database.listFridges(this, new FridgeListListener() {
            @Override
            public void onListResult(List<String> fridgeNames) {

                autoCompleteAdapter = new ArrayAdapter<>(FridgeSettingActivity.this,
                        android.R.layout.simple_dropdown_item_1line, fridgeNames);
                //autoCompleteFridgeTextView.setAdapter(autoCompleteAdapter);

                autoCompleteFridgeTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //autoCompleteFridgeTextView.showDropDown();
                    }
                });



            }
            private void joinFridge(String fridgeName) {
                Database.joinFridge(FridgeSettingActivity.this, fridgeName, new OperationCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(FridgeSettingActivity.this, "Joined fridge successfully", Toast.LENGTH_SHORT).show();

                        // Redirecting user to FridgeDetailActivity
                        Intent intent = new Intent(FridgeSettingActivity.this, FridgeDetailActivity.class);
                        intent.putExtra("FRIDGE_NAME", fridgeName);
                        startActivity(intent);

                        finish();


                    }

                    @Override
                    public void onCanceled() {
                        // Handling the operation being cancelled
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handling the failure case, such as if the fridge does not exist
                        Toast.makeText(FridgeSettingActivity.this, "Failed to join fridge: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCanceled() {
                // Handle the canceled case
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure case
            }
        });
    }



    // toggling the Personal Settings section
    public void toggleNewFridge(View view) {
        // Toggling the visibility of the expandable layout
        boolean isVisible = layoutNotificationSettings.getVisibility() == View.VISIBLE;
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

    /**
     * On checked changed.
     *
     * @param buttonView the button view
     * @param isChecked  the is checked
     */
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Handle switch toggle
        if(isChecked) {
            // Code to handle notifications allowed
        } else {
            // Code to handle notifications disallowed
        }
    }
    // Method to toggle the Notification Settings section


}
