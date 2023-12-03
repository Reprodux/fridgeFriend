package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fridge_friend.toolbar.AppToolbar;

import java.util.ArrayList;
import java.util.List;

public class FridgeListActivity extends AppToolbar implements FridgeAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private FridgeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_list);

        recyclerView = findViewById(R.id.recyclerViewFridges);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetching fridge names
        List<String> fridgeNames = getFridgeNames();

        // setting up RecyclerView with the data
        adapter = new FridgeAdapter(this, fridgeNames);
        adapter.setClickListener(this); // Set the click listener for the adapter
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFridgeClick(View view, int position) {
        // Handling the fridge click that opens FridgeDetailActivity
        Intent intent = new Intent(FridgeListActivity.this, FridgeDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onJoinFridgeClick(View view, int position) {
        // Handling the join fridge click (e.g., update the database)
    }

    private List<String> getFridgeNames() {
        //Implement database or network call to fetch fridge names
        // Temporary stub for development
        List<String> fridgeNames = new ArrayList<>();
        fridgeNames.add("ChillMaster 3000");
        fridgeNames.add("Frosty's Safe Haven");
        fridgeNames.add("Arctic Enclave");
        fridgeNames.add("The Cool Keeper");
        fridgeNames.add("Icy Storage Unit");
        return fridgeNames;
    }

    //Overrides toolbars about to display info on current activity
    @Override
    public void about() {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder((FridgeListActivity.this));
        alert_builder.setTitle(R.string.fridge_list_title).setMessage(R.string.fridgeListAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }
}
