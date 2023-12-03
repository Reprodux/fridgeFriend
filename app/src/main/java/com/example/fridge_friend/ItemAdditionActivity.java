package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fridge_friend.toolbar.AppToolbar;

/**
 * The type Item addition activity.
 */
public class ItemAdditionActivity extends AppToolbar {

    private EditText editTextItemName;
    private EditText editTextItemQuantity;
    private EditText editTextExpiryDate;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_addition);

        // Initialize the EditTexts
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);

        // Initialize the Save Button
        buttonSave = findViewById(R.id.buttonSave);

        // Setting the button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        // Retrieve the input from the EditText fields
        String itemName = editTextItemName.getText().toString().trim();
        String itemQuantity = editTextItemQuantity.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        // Validate the input
        if (itemName.isEmpty() || itemQuantity.isEmpty() || expiryDate.isEmpty()) {
            // Showing error, one or more fields are empty
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        //Implement the logic to save the item to the database

        // For now, just showing a confirmation message
        Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();

        //finishing the activity to return to the previous screen
        finish();
    }

    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ItemAdditionActivity.this));
        alert_builder.setTitle(R.string.item_addition_title).setMessage(R.string.itemAddAbout);
        alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }
}
