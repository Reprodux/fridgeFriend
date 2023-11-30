package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.HashMap;
import java.util.List;

public class barcodeScanner extends AppToolbar implements barcode_data_retrieval.response {


    private Button start_scan_btn;
    private TextView barcode_txt;



    private final ActivityResultLauncher<ScanOptions> barcode_scanner = registerForActivityResult(new ScanContract(),
            result -> {
                //Popup for progress bar substitute
                ProgressDialog progressPopup = new ProgressDialog(this);
                progressPopup.setMessage("Retrieving data...."); // msg dialog
                progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                progressPopup.setMax(100);
                progressPopup.setCancelable(false);
                progressPopup.show(); // Display Progress Dialog



                if (result.getContents() == null) {
                    Toast.makeText(barcodeScanner.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    progressPopup.show(); // Display Progress Dialog
                    progressPopup.incrementProgressBy(60);
                    Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    barcode_txt = findViewById(R.id.barcode_text);

                    //Alert Dialog to confirm scan -> enable only for testing purposes
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder((barcodeScanner.this));
                    alert_builder.setTitle("Barcode scanned: ").setMessage(result.getContents());
                    alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
                        //Usr clicked OK
                        Log.i(TAG, "User acknowledged barcode scan");

                    });
                    progressPopup.incrementProgressBy(20);
                    //alert_builder.show();

                    barcode_txt.setText(result.getContents());

                    //Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    new barcode_data_retrieval(this).execute(result.getContents());
                    progressPopup.incrementProgressBy(20);
                    progressPopup.dismiss();


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
            product_info += "Name: " + product_name + "\n";

            if (product_code != null) {
                product_info += "Code: " + product_code + "\n";
            }

            if (product_categories != null) {
                product_info += "Categories: " + "\n";

                // Get shortest category
                int shortest = 100;
                int shortest_index = 0;
                for (Object category : product_categories) {
                    if (category.toString().length() < shortest) {
                        shortest = category.toString().length();
                        shortest_index = product_categories.indexOf(category);
                    }
                }

                product_info += "   " + capitializeWords(product_categories.get(shortest_index).toString()) + "\n";
            }

            if (brands != null) {
                product_info += "Brands: " + "\n";
                for (Object brand : brands) {
                    product_info += "   " + capitializeWords(brand.toString()) + "\n";
                    break;
                }
            }

            if (product_facts != null) {
                product_info += "Facts (per 100g): " + "\n";
                for (Object key : product_facts.keySet()) {
                    String cleanKey = key.toString().replaceAll("_100g", "");
                    cleanKey = cleanKey.replaceAll("-", " ");

                    product_info += "   " + capitializeWords(cleanKey) + ": " + Math.round(Float.parseFloat(product_facts.get(key).toString())) + "\n";
                }
            }
        }

        barcode_txt.setText(product_info);

        Log.i(TAG, "processFinish in barcode scanner activity run");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        barcode_txt = findViewById(R.id.barcode_text);
        start_scan_btn = findViewById(R.id.start_scan_btn);

        start_scan_btn.setOnClickListener((view -> {
            ScanOptions options = new ScanOptions();
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
            options.setOrientationLocked(false);
            options.setBeepEnabled(true);
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