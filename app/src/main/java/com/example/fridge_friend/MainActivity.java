package com.example.fridge_friend;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fridge_friend.toolbar.AppToolbar;

public class MainActivity extends AppToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMyFridge(View v) {
        // TODO: Launch My Fridge Activity
        showToast(R.string.my_fridges_clicked);
    }

    public void onNewFridge(View v) {
        // TODO: Launch New Fridge Activity
        showToast(R.string.new_fridge_clicked);
    }

    public void onBarcodeScan(View v) {
        // TODO: Launch Barcode Scanning Activity
        showToast(R.string.barcode_scanner_clicked);
    }

    public void onShoppingList(View v) {
        // TODO: Launch Shopping List Activity
        showToast(R.string.shopping_list_clicked);
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

}