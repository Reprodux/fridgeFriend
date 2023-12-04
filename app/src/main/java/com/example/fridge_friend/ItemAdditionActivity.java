package com.example.fridge_friend;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.Item;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.toolbar.AppToolbar;

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
        String itemQuantityString = editTextItemQuantity.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        // Validate the input
        if (itemName.isEmpty() || itemQuantityString.isEmpty() || expiryDate.isEmpty()) {
            // Showing error, one or more fields are empty
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        // Converting the quantity string to a long
        long itemQuantity;
        try {
            itemQuantity = Long.parseLong(itemQuantityString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number for quantity", Toast.LENGTH_LONG).show();
            return;
        }

        //Implementing the logic to save the item to the database
        // Create a new Item object
        Item newItem = new Item(itemName, itemQuantity, expiryDate);
        // Get the fridge ID from the intent or from a selected item in the previous activity
        String fridgeId = getIntent().getStringExtra("EXTRA_FRIDGE_ID");
        if(fridgeId == null || fridgeId.isEmpty()) {
            Log.e("FridgeError", "Fridge ID is null or empty");
            Toast.makeText(this, "Error: Fridge ID not found", Toast.LENGTH_LONG).show();
            return;
        }

        // Save the item to the database
        Database.addItem(this, fridgeId, newItem, new OperationCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ItemAdditionActivity.this, "Item saved successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity and go back
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ItemAdditionActivity.this, "Error saving item: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCanceled() {
                Toast.makeText(ItemAdditionActivity.this, "Item saving canceled", Toast.LENGTH_LONG).show();
            }
        });


    }
}
