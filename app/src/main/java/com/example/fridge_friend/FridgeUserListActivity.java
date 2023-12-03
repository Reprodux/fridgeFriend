package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.UserListListener;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Fridge user list activity.
 *
 * @noinspection deprecation
 */
public class FridgeUserListActivity extends AppToolbar implements FridgeUsersAdapter.ItemClickListener {

    private FridgeUsersAdapter adapter;
    private final Map<String, String> userMap = new HashMap<>();
    private ProgressDialog progressPopup;
    private String fridgeName;

    @SuppressLint("NotifyDataSetChanged")
    private final ActivityResultLauncher<Intent> detailsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            String userId = result.getData().getStringExtra("userId");
            String operation = result.getData().getStringExtra("operation");
            if ("leave".equals(operation)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                userMap.remove(userId);
                adapter.notifyDataSetChanged();
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            finish();
            return;
        }

        if (getIntent() != null) {
            fridgeName = getIntent().getStringExtra("fridgeName");
        }
        if (fridgeName == null) {
            fridgeName = "test";
        }

        TextView fridgeHeader = findViewById(R.id.textViewFridgeNameHeader);
        fridgeHeader.setText(fridgeName);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUserList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FridgeUsersAdapter(this, userMap);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        progressPopup = new ProgressDialog(this);
        progressPopup.setMessage(getString(R.string.please_wait));
        progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressPopup.setMax(100);
        progressPopup.setCancelable(false);
        progressPopup.show();

        Database.getUsersInFridge(this, fridgeName, new LoadingListener(this));
    }

    // ItemClickListener implementation
    @Override
    public void onItemClick(int position) {
        // Get the clicked user id
        String userId = adapter.getUserId(position);
        // Start the ItemDetailActivity
        Intent detailIntent = new Intent(this, UserDetailsActivity.class);
        String user = userMap.get(userId);
        detailIntent.putExtra("userId", userId);
        detailIntent.putExtra("userName", user);
        detailIntent.putExtra("fridgeName", fridgeName);
        detailsLauncher.launch(detailIntent);
    }

    private class LoadingListener implements UserListListener {
        private final FridgeUserListActivity activity;

        /**
         * Instantiates a new Loading listener.
         *
         * @param activity the activity
         */
        LoadingListener(FridgeUserListActivity activity) {
            this.activity = activity;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onResult(Map<String,String> userIds) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            activity.userMap.clear();
            activity.userMap.putAll(userIds);
            activity.adapter.notifyDataSetChanged();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("FridgeUserList", "Loading Error!", e);
            activity.progressPopup.dismiss();
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_loading_users);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

    }
    //Overrides toolbars about to display info on current activity
    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((FridgeUserListActivity.this));
        alert_builder.setTitle(R.string.fridge_userList_title).setMessage(R.string.fridgeUserListAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }

}
