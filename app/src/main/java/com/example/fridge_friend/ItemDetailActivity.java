package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fridge_friend.toolbar.AppToolbar;

/**
 * The type Item detail activity.
 */
public class ItemDetailActivity extends AppToolbar {

    /**
     * The constant EXTRA_ITEM_ID.
     */
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private TextView textViewWelcomeUser;
    private TextView textViewItemOwner;
    private TextView textViewItemQuantity;
    private TextView textViewExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Bind the TextViews from the layout
        textViewWelcomeUser = findViewById(R.id.textViewWelcomeUser);
        textViewItemOwner = findViewById(R.id.textViewItemOwner);
        textViewItemQuantity = findViewById(R.id.textViewItemQuantity);
        textViewExpiryDate = findViewById(R.id.textViewExpiryDate);

        // Replace these with actual data retrieval logic
        // For demonstration, we're setting dummy data
        String dummyOwner = "John Doe";
        String dummyItemName = "Eggs";
        String dummyQuantity = "50";
        String dummyExpiryDate = "2023-05-15";

        textViewWelcomeUser.setText(dummyItemName);
        textViewItemOwner.setText(getString(R.string.item_owner, dummyOwner));
        textViewItemQuantity.setText(getString(R.string.item_quantity, dummyQuantity));
        textViewExpiryDate.setText(getString(R.string.item_expiry, dummyExpiryDate));
    }

    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ItemDetailActivity.this));
        alert_builder.setTitle(R.string.item_detail_title).setMessage(R.string.itemDetailAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");
        }).show();
    }
}