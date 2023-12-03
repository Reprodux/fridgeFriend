package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fridge_friend.database.local.CartDatabase;
import com.example.fridge_friend.database.local.CartDatabaseHelper;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The type Main activity.
 */
public class MainActivity extends AppToolbar {
    /**
     * The Cart db.
     */
    CartDatabaseHelper cartDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Homepage");
    }

    /**
     * Check data.
     *
     * @param V the v
     */
    public void checkData(View V){
        FirebaseAuth.getInstance().signOut();
        Log.i(TAG, String.valueOf(R.string.signed_out));
        showToast(R.string.signed_out);
        finish();
    }

    /**
     * On my fridge.
     *
     * @param v the v
     */
    public void onMyFridge(View v) {
        // TODO: Launch My Fridge Activity
        Log.i(TAG, String.valueOf(
                R.string.my_fridges_clicked));
        showToast(R.string.my_fridges_clicked);
        Intent intent = new Intent(this, FridgeListActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * On new fridge.
     *
     * @param v the v
     */
    public void onNewFridge(View v) {
        // TODO: Launch New Fridge Activity
        Intent intent = new Intent(this, FridgeSettingActivity.class);
        startActivity(intent);

    }

    /**
     * On barcode scan.
     *
     * @param v the v
     */
    public void onBarcodeScan(View v) {
        Log.i(TAG, String.valueOf(R.string.barcode_scanner_clicked));
        showToast(R.string.barcode_scanner_clicked);

        Intent intent = new Intent(this, barcodeScanner.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * On shopping list.
     *
     * @param v the v
     */
    public void onShoppingList(View v) {
        // TODO: Launch Shopping List Activity
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }
    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((MainActivity.this));
        alert_builder.setTitle(R.string.main_title).setMessage(R.string.mainAbout);
        alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }
    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}