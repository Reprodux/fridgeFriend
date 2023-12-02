package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.database.local.CartDatabase;
import com.example.fridge_friend.database.local.ShoppingCartItem;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/** @noinspection deprecation*/
public class barcodeScanner extends AppToolbar implements barcode_data_retrieval.response {


    private Button start_scan_btn;
    private TextView barcode_txt;
    private ProgressDialog progressPopup;
    ProgressBar barcode_bar;


    private final ActivityResultLauncher<ScanOptions> barcode_scanner = registerForActivityResult(new ScanContract(),
            result -> {
                barcode_bar.setVisibility(View.VISIBLE);
                barcode_bar.setProgress(50);
                if (result.getContents() == null) {
                    Toast.makeText(barcodeScanner.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    //Popup for progress bar substitute

                    Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    barcode_txt.setText(getString(R.string.retrieving_data));

                    //Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    barcode_bar.setProgress(75);
                    new barcode_data_retrieval(this).execute(result.getContents());



                }
            });

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
            barcode_bar.setProgress(100);
            barcode_bar.setVisibility(View.INVISIBLE);
        }

        barcode_txt.setGravity(0);
        barcode_txt.setText(Html.fromHtml(product_info));
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        barcode_txt = findViewById(R.id.barcode_text);
        start_scan_btn = findViewById(R.id.start_scan_btn);
        barcode_bar = findViewById(R.id.barcode_bar);
        barcode_bar.setVisibility(View.INVISIBLE);
        start_scan_btn.setOnClickListener((view -> {

            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
            options.setOrientationLocked(false);
            options.setBeepEnabled(true);
            barcode_bar.setProgress(25);
            barcode_scanner.launch(new ScanOptions());

            //startBarcodeScanner();
        }));

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}