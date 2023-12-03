package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.FridgeListListener;
import com.example.fridge_friend.database.local.CartDatabase;
import com.example.fridge_friend.database.local.ShoppingCartItem;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

/**
 * The type Shopping item view activity.
 */
public class ShoppingItemViewActivity extends AppToolbar implements barcode_data_retrieval.response, FridgeListListener {

    private TextView textViewWelcomeUser;
    private TextView textViewItemName;
    private TextView textViewItem;
    private TextView textViewExpiryDate;
    private Button addToFridge;
    private ProgressBar item_load_bar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_barcode_data);

        //TODO: replace all this with formatted JSON code retrieved from API/Database
        //assume that intent.getStringExtra("barcode_str") should return the 13 digit barcode
        Intent intent = getIntent();
        String upc = intent.getStringExtra("upc");
        textViewItem = findViewById(R.id.itemData);
        item_load_bar = findViewById(R.id.item_load_bar);
        item_load_bar.setVisibility(View.VISIBLE);
        item_load_bar.setProgress(50);
        addToFridge = findViewById(R.id.addToFridge);


        //Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        item_load_bar.setProgress(75);

        Log.d(TAG,"ITEM ID DETAILS " +upc);
        textViewItemName= findViewById(R.id.itemName);
        new barcode_data_retrieval(this).execute(upc);

        //create an arraylist of user joined fridges





        addToFridge.setOnClickListener(view -> {
            Database.listFridges(this, this);


        });


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ShoppingItemViewActivity.this));
        alert_builder.setTitle(R.string.shopping_item_detail_title).setMessage(R.string.shoppingItemDetailAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }
    /**
     * Capitialize words string.
     *
     * @param word the word
     * @return the string
     */
    public String capitializeWords(String word){
        String[] words = word.split(" ");
        String cleanWord = "";
        for (String w : words) {
            cleanWord += w.substring(0, 1).toUpperCase() + w.substring(1) + " ";
        }
        cleanWord = cleanWord.trim();
        return cleanWord;
    }
    @Override
    public void processFinish(String product_name, String product_code, List product_categories, List brands, HashMap product_facts) {
        Snackbar.make(findViewById(android.R.id.content), product_name, Snackbar.LENGTH_SHORT).show();



        String product_info = "";

        if (product_name != null) {
            product_info += "<b>Name: </b>" + product_name + "<br>";

            if (product_code != null) {
                product_info += "<b>Code: </b>" + product_code + "<br>";
            }

            if (product_categories != null) {
                product_info += "<b>Categories: </b>" + "<br>";

                // Get shortest category
                int shortest = 100;
                int shortest_index = 0;
                for (Object category : product_categories) {
                    if (category.toString().length() < shortest) {
                        shortest = category.toString().length();
                        shortest_index = product_categories.indexOf(category);
                    }
                }

                product_info += "   " + capitializeWords(product_categories.get(shortest_index).toString()) + "<br>";
            }

            if (brands != null) {
                product_info += "<b>Brands: </b>" + "<br>";
                for (Object brand : brands) {
                    product_info += "   " + capitializeWords(brand.toString()) + "<br>";
                    break;
                }
            }

            if (product_facts != null) {
                product_info += "<b>Facts (per 100g): </b>" + "<br>";
                for (Object key : product_facts.keySet()) {
                    String cleanKey = key.toString().replaceAll("_100g", "");
                    cleanKey = cleanKey.replaceAll("-", " ");

                    product_info += "   <b>" + capitializeWords(cleanKey) + ": </b>" + Math.round(Float.parseFloat(product_facts.get(key).toString())) + "<br>";
                }
            }
            item_load_bar.setProgress(100);
            item_load_bar.setVisibility(View.INVISIBLE);
        }

        textViewItem.setGravity(0);
        textViewItem.setText(Html.fromHtml(product_info));
        assert product_code != null;
        ShoppingCartItem item = new ShoppingCartItem(product_name, 1, product_code);

        boolean result = CartDatabase.storeItem(this, item);
        long id = item.getId();
        String upc = item.getUPC();
        Log.i(TAG, "Stored item in database: " + String.valueOf(result));
        Log.i(TAG, "Stored item id: " + String.valueOf(id));
        Log.i(TAG, "Stored item id: " + String.valueOf(upc));
        Log.i(TAG, "processFinish in barcode scanner activity run");
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onCanceled() {

    }

    @Override
    public void onListResult(List<String> result) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingItemViewActivity.this);
        builder.setTitle(getString(R.string.add_to_fridge));
        //alertDialog builder should have radio buttons with each row being an element from result
        //multiple choice buttons in builder
        //convert result to arraylist



    }
}