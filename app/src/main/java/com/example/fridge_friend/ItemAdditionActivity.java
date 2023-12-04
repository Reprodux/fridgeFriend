package com.example.fridge_friend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.fridge_friend.database.listener.UserNameListener;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class ItemAdditionActivity extends AppToolbar {

    private EditText editTextItemName;
    private EditText editTextItemQuantity;
    private EditText editTextExpiryDate;
    private Button buttonSave;

    private String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_addition);

        // Initialize the EditTexts
        editTextItemName = findViewById(R.id.editTextItemName);
        editTextItemQuantity = findViewById(R.id.editTextItemQuantity);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextExpiryDate.addTextChangedListener(tw);

        // Initialize the Save Button
        buttonSave = findViewById(R.id.buttonSave);
        String uid = FirebaseAuth.getInstance().getUid();
        Database.getUserName(this, uid, new ItemAdditionActivity.LoadingListener(this));
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK,
                new Intent().putExtra("EXTRA_FRIDGE_NAME", intent.getStringExtra("EXTRA_FRIDGE_NAME")));
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
        Item newItem = new Item(itemName, itemQuantity, expiryDate, owner);
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
    private class LoadingListener implements UserNameListener {

        private final ItemAdditionActivity activity;

        /**
         * Instantiates a new Loading listener.
         *
         * @param activity the activity
         */
        LoadingListener( ItemAdditionActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onResult(String name, String id) {
            owner = name;
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("ShoppingItemViewActivity", "Loading Error!", e);
            androidx.appcompat.app.AlertDialog.Builder errorAlert = new androidx.appcompat.app.AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_loading_name);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

    }
    TextWatcher tw = new TextWatcher() {
        private String cur = "";
        private String format_date = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        @Override
        public void onTextChanged(CharSequence str, int start, int before, int count) {
            if (!str.toString().equals(cur)) {
                String clean = str.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = cur.replaceAll("[^\\d.]|\\.", "");

                int len = clean.length();
                int selection = len;
                for (int i = 2; i <= len && i < 6; i += 2) {
                    selection++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) selection--;

                if (clean.length() < 8){
                    clean = clean + format_date .substring(clean.length());
                }else{
                    //Ensures proper inputting of numbers
                    int day  = Integer.parseInt(clean.substring(0,2));
                    int mon  = Integer.parseInt(clean.substring(2,4));
                    int year = Integer.parseInt(clean.substring(4,8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);

                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    clean = String.format("%02d%02d%02d",day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                selection = selection < 0 ? 0 : selection;
                cur = clean;
                editTextExpiryDate.setText(cur);
                editTextExpiryDate.setSelection(selection < cur.length() ? selection : cur.length());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };

}
