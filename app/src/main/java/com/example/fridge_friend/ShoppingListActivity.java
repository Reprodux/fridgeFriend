package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.database.local.CartDatabase;
import com.example.fridge_friend.database.local.ShoppingCartItem;
import com.example.fridge_friend.toolbar.AppToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Shopping list activity.
 */
public class ShoppingListActivity extends AppToolbar implements ShoppingCartItemsAdapter.ItemClickListener {

    private ShoppingCartItemsAdapter adapter;

    private List<ShoppingCartItem> shoppingItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        getSupportActionBar().setTitle(getString(R.string.shopping_list));
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    // ItemClickListener implementation
    @Override
    public void onItemClick(int position) {
        // Get the clicked item
        ShoppingCartItem item = shoppingItems.get(position);
        // Start the ItemDetailActivity
        Intent detailIntent = new Intent(this, ShoppingItemViewActivity.class);
        String upc = item.getUPC();
        detailIntent.putExtra("upc", upc);
        Log.d(TAG,"ITEM ID sla " + upc);
        startActivity(detailIntent);
    }

    private List<ShoppingCartItem> createStubFridgeItemList() {
        // Create a stub list of fridge items
        List<ShoppingCartItem> items = new ArrayList<>();
        items = CartDatabase.getItems(this);
        return items;
    }
    @Override
    public void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewShoppingItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingItems = createStubFridgeItemList();

        adapter = new ShoppingCartItemsAdapter(this, shoppingItems);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        Button buttonClearList = findViewById(R.id.buttonClearShoppingList);
        buttonClearList.setOnClickListener(v -> {
            int size = shoppingItems.size();
            CartDatabase.clearCart(this);
            shoppingItems.clear();
            adapter.notifyItemRangeRemoved(0, size);
        });
    }
    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ShoppingListActivity.this));
        alert_builder.setTitle(R.string.shopping_list_title).setMessage(R.string.shoppingListAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }

}
