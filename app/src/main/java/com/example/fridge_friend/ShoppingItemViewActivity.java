package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fridge_friend.toolbar.AppToolbar;

public class ShoppingItemViewActivity extends AppToolbar {

    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private TextView textViewWelcomeUser;
    private TextView textViewItemName;
    private TextView textViewItemupc;
    private TextView textViewExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_barcode_data);

        //TODO: replace all this with formatted JSON code retrieved from API/Database
        //assume that intent.getStringExtra("barcode_str") should return the 13 digit barcode
        Intent intent = getIntent();
        String upc = intent.getStringExtra("upc");


        Log.d(TAG,"ITEM ID DETAILS " +upc);
        textViewItemName= findViewById(R.id.itemName);

        textViewItemupc = findViewById(R.id.itemData);
        textViewItemupc.setText(intent.getStringExtra("upc"));



    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ShoppingItemViewActivity.this));
        alert_builder.setTitle(R.string.shopping_item_detail_title).setMessage(R.string.shoppingItemDetailAbout);
        alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }
}