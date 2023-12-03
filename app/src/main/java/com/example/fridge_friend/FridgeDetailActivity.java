package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.toolbar.AppToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Fridge detail activity.
 */
public class FridgeDetailActivity extends AppToolbar implements FridgeItemsAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private FridgeItemsAdapter adapter;
    private List<FridgeItem> fridgeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_detail);

        recyclerView = findViewById(R.id.recyclerViewFridgeItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fridgeItems = createStubFridgeItemList();
        adapter = new FridgeItemsAdapter(this, fridgeItems);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Button buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ItemAdditionActivity
                Intent addItemIntent = new Intent(FridgeDetailActivity.this, ItemAdditionActivity.class);
                startActivity(addItemIntent);
            }
        });
    }

    // ItemClickListener implementation
    @Override
    public void onItemClick(int position) {
        // Get the clicked item
        FridgeItem item = fridgeItems.get(position);
        // Start the ItemDetailActivity
        Intent detailIntent = new Intent(this, ItemDetailActivity.class);
        detailIntent.putExtra(ItemDetailActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(detailIntent);
    }

    private List<FridgeItem> createStubFridgeItemList() {
        // Create a stub list of fridge items
        List<FridgeItem> items = new ArrayList<>();
        items.add(new FridgeItem("1", "Apples", "Expires on: 2023-05-01"));
        items.add(new FridgeItem("2", "Eggs", "12 pack, free-range"));
        items.add(new FridgeItem("3", "Pizza", "Cheddar, 200g"));
        // Add more items as needed
        return items;
    }

    //Overrides toolbars about to display info on current activity
    @Override
    public void about() {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder((FridgeDetailActivity.this));
        alert_builder.setTitle(R.string.fridge_detail_ac_title).setMessage(R.string.fridgeDetailAbout);
        alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}
