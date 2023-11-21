package com.example.fridge_friend;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fridge_friend.toolbar.AppToolbar;

public class ShoppingItemViewActivity extends AppToolbar {

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private TextView textViewWelcomeUser;
    private TextView textViewItemOwner;
    private TextView textViewItemQuantity;
    private TextView textViewExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        //TODO: replace all this with formatted JSON code retrieved from API/Database


        // Bind the TextViews from the layout

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
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}