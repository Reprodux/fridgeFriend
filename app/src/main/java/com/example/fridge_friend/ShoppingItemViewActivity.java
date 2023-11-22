package com.example.fridge_friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fridge_friend.toolbar.AppToolbar;

public class ShoppingItemViewActivity extends AppToolbar {

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private TextView textViewWelcomeUser;
    private TextView textViewItemName;
    private TextView textViewItemQuantity;
    private TextView textViewExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_barcode_data);

        //TODO: replace all this with formatted JSON code retrieved from API/Database
        //assume that intent.getStringExtra("barcode_str") should return the 13 digit barcode



        // Bind the TextViews from the layout

        textViewItemName= findViewById(R.id.itemName);
        textViewItemQuantity = findViewById(R.id.itemData);



    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}