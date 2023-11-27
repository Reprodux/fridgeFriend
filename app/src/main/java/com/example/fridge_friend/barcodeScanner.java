package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.toolbar.AppToolbar;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class barcodeScanner extends AppToolbar implements AsyncResponse {
    private Button start_scan_btn;
    private TextView barcode_txt;


    private final ActivityResultLauncher<ScanOptions> barcode_scanner = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null){
                    Toast.makeText(barcodeScanner.this, "Cancelled", Toast.LENGTH_LONG).show();
                        } else {

                    Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    barcode_txt = findViewById(R.id.barcode_text);
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder((barcodeScanner.this));
                    alert_builder.setTitle("Barcode scanned: ").setMessage(result.getContents());
                    alert_builder.setPositiveButton("Ok", (dialogInterface, id) -> {
                        //Usr clicked OK
                        Log.i(TAG, "User acknowledged barcode scan");

                    });

                    alert_builder.show();
                    barcode_txt.setText(result.getContents());
                    Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                    barcode_data_retrieval bdr = new barcode_data_retrieval();
                    bdr.delegate = this;
                    bdr.execute(result.getContents());


                }
            });
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

    @Override
    public void processFinish(String product_valid, String product_name, String product_code, String product_categories, String brands, String product_facts) {
        // Log the variables
        Log.i(TAG, "Product Valid: " + product_valid);
        Log.i(TAG, "Product Name: " + product_name);
        Log.i(TAG, "Product Code: " + product_code);
        Log.i(TAG, "Product Categories: " + product_categories);
        Log.i(TAG, "Product Brands: " + brands);
        Log.i(TAG, "Product Facts: " + product_facts);

        // Toast the variables
        Toast.makeText(barcodeScanner.this, "Product Valid: " + product_valid, Toast.LENGTH_LONG).show();
        Toast.makeText(barcodeScanner.this, "Product Name: " + product_name, Toast.LENGTH_LONG).show();
        Toast.makeText(barcodeScanner.this, "Product Code: " + product_code, Toast.LENGTH_LONG).show();
        Toast.makeText(barcodeScanner.this, "Product Categories: " + product_categories, Toast.LENGTH_LONG).show();
        Toast.makeText(barcodeScanner.this, "Product Brands: " + brands, Toast.LENGTH_LONG).show();
        Toast.makeText(barcodeScanner.this, "Product Facts: " + product_facts, Toast.LENGTH_LONG).show();
    }
}