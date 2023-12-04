package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.Item;
import com.example.fridge_friend.database.listener.ItemListener;
import com.example.fridge_friend.toolbar.AppToolbar;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Fridge detail activity.
 */
public class FridgeDetailActivity extends AppToolbar implements FridgeItemsAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private FridgeItemsAdapter adapter;
    private List<Item> items;
    private String fridgeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_detail);

        //setting up recycle view and adapter
        recyclerView = findViewById(R.id.recyclerViewFridgeItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        items = new ArrayList<>();
        adapter = new FridgeItemsAdapter(this, items);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // Getting the fridge name from the intent
        fridgeId = getIntent().getStringExtra("EXTRA_FRIDGE_NAME");


        if (fridgeId != null) {
            loadFridgeItems(fridgeId);
            Log.d("FridgeDetailActivity", "Fridge ID retrieved from intent: " + fridgeId);
        } else {
            Log.e("FridgeDetailActivity", "Fridge name not found in intent extras");
            // Optionally, show a Toast message to inform the user
            Toast.makeText(this, "Error: Fridge name not found", Toast.LENGTH_SHORT).show();
        }

        Button buttonAddItem = findViewById(R.id.buttonAddItem);

        buttonAddItem.setOnClickListener(v -> {
            Intent addItemIntent = new Intent(FridgeDetailActivity.this, ItemAdditionActivity.class);
            addItemIntent.putExtra("EXTRA_FRIDGE_ID", fridgeId);
            startActivity(addItemIntent);
        });
    }
    private void loadFridgeItems(String fridgeId) {
        Log.d("FridgeDetailActivity", "Loading fridge items for Fridge ID: " + fridgeId);
        // Call the database method to get the items for the given fridge ID
        Database.getItems(this, fridgeId, new ItemListener() {


            @Override
            public void onResult(Map<String, Item> result) {
                items.clear();
                if (result != null) {
                    items.addAll(result.values());
                } else {
                    // Handle the case where result is null, maybe show a message
                    Log.d("FridgeDetailActivity", "No items found for this fridge.");
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCanceled() {
                // Handling cancellation
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                // Handling failure
            }
        });
    }



    // ItemClickListener implementation
    @Override
    public void onItemClick(int position) {
        // Get the clicked item
        Item selectedItem = items.get(position);
        // Start the ItemAdditionActivity
        Intent addItemIntent = new Intent(this, ItemAdditionActivity.class);
        Intent itemDetailIntent = new Intent(this, ItemDetailActivity.class);

        addItemIntent.putExtra("EXTRA_ITEM_NAME", selectedItem.getName());
        // Pass the fridge ID to the ItemAdditionActivity
        addItemIntent.putExtra("EXTRA_FRIDGE_ID", fridgeId); // Make sure fridgeId is the ID of the current fridge
        startActivity(addItemIntent);
        itemDetailIntent.putExtra("EXTRA_ITEM_NAME", selectedItem.getName());
        itemDetailIntent.putExtra("EXTRA_ITEM_QUANTITY", selectedItem.getAmount());
        itemDetailIntent.putExtra("EXTRA_ITEM_EXPIRY_DATE", selectedItem.getExpiry());
        startActivity(itemDetailIntent);
    }



    //Overrides toolbars about to display info on current activity
    @Override
    public void about() {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder((FridgeDetailActivity.this));
        alert_builder.setTitle(R.string.fridge_detail_ac_title).setMessage(R.string.fridgeDetailAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");
        }).show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}