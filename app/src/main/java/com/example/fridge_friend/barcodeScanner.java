package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;

public class barcodeScanner extends AppToolbar {


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
}