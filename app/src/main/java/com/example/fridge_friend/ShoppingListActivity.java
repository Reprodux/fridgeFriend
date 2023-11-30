package com.example.fridge_friend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.database.local.CartDatabase;
import com.example.fridge_friend.database.local.ShoppingCartItem;
import com.example.fridge_friend.toolbar.AppToolbar;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppToolbar implements ShoppingCartItemsAdapter.ItemClickListener {

    private ShoppingCartItemsAdapter adapter;
    private List<ShoppingCartItem> shoppingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewShoppingItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingItems = CartDatabase.getItems(this);
        if (shoppingItems.size() == 0) {
            shoppingItems = createStubFridgeItemList();
        }
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
        detailIntent.putExtra(ItemDetailActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(detailIntent);
    }

    private List<ShoppingCartItem> createStubFridgeItemList() {
        // Create a stub list of fridge items
        List<ShoppingCartItem> items = new ArrayList<>();
        items.add(new ShoppingCartItem("Apples", 5));
        items.add(new ShoppingCartItem("Eggs", 12));
        items.add(new ShoppingCartItem("Cheese Pizza", 1));
        // items.add(new FridgeItem("2", "Eggs", "12 pack, free-range"));
        // Add more items as needed
        CartDatabase.storeItems(this, items);
        return items;
    }

}
