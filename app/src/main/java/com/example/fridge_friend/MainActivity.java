package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkData(View V){
        FirebaseAuth.getInstance().signOut();
        Log.i(TAG, String.valueOf(R.string.signed_out));
        showToast(R.string.signed_out);
        finish();
    }

    public void onMyFridge(View v) {
        // TODO: Launch My Fridge Activity
        Log.i(TAG, String.valueOf(R.string.my_fridges_clicked));
        showToast(R.string.my_fridges_clicked);
    }

    public void onNewFridge(View v) {
        // TODO: Launch New Fridge Activity
        Log.i(TAG, String.valueOf(R.string.new_fridge_clicked));
        showToast(R.string.new_fridge_clicked);
    }

    public void onBarcodeScan(View v) {
        Log.i(TAG, String.valueOf(R.string.barcode_scanner_clicked));
        showToast(R.string.barcode_scanner_clicked);

        Intent intent = new Intent(this, barcodeScanner.class);
        MainActivity.this.startActivity(intent);
    }

    public void onShoppingList(View v) {
        // TODO: Launch Shopping List Activity
        Log.i(TAG, String.valueOf(R.string.shopping_list_clicked));
        showToast(R.string.shopping_list_clicked);
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }
}