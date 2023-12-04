package com.example.fridge_friend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.FridgeListListener;
import com.example.fridge_friend.toolbar.AppToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The type Fridge list activity.
 */
public class FridgeListActivity extends AppToolbar implements FridgeAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private FridgeAdapter adapter;

    private String selectedFridgeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_list);

        recyclerView = findViewById(R.id.recyclerViewFridges);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initializing an adapter with an empty list
        adapter = new FridgeAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Setting the click listener for the adapter
        adapter.setClickListener(this);
        ProgressDialog progressPopup; // Progress Dialog Object
        progressPopup = new ProgressDialog(this);
        progressPopup.setMessage(getString(R.string.retrieving_data)); // msg dialog
        progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressPopup.setCancelable(false);
        progressPopup.show();

        Database.listFridges(this, new FridgeListListener(){
            @Override
            public void onListResult(List<String> fridgeNames){
                //updating the adapter with the retrieved fridge names
                adapter.updateData(fridgeNames);
                progressPopup.dismiss();
            }

            @Override
            public void onCanceled(){
                //handling operation when cancelled
            }
            @Override
            public void onFailure(@NonNull Exception e){
                //handling failure in fetching fridge names
            }

        });
    }

    @Override
    public void onFridgeClick(View view, int position) {
        // Get the fridge name from the adapter using the position
        selectedFridgeId = adapter.getItem(position);
        Log.d("FridgeListActivity", "Selected Fridge ID: " + selectedFridgeId);

        // Create an intent for starting the FridgeDetailActivity
        Intent intent = new Intent(FridgeListActivity.this, FridgeDetailActivity.class);

        // Put the fridge name into the intent as an extra
        intent.putExtra("EXTRA_FRIDGE_NAME", selectedFridgeId);

        // Start the FridgeDetailActivity with the intent
        startActivity(intent);

    }

    @Override
    public void onJoinFridgeClick(View view, int position) {
        // Handling the plus click that opens item addition form
        Intent intent = new Intent(FridgeListActivity.this, ItemAdditionActivity.class);

        intent.putExtra("EXTRA_FRIDGE_ID", selectedFridgeId); // Pass the selected fridge ID
        Log.d("FridgeListActivity", "Passing Fridge ID to ItemAdditionActivity: " + selectedFridgeId);
        startActivity(intent);
    }


}
