package com.example.fridge_friend;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
//import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;

public class barcodeScanner extends AppToolbar {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private Button start_scan_btn;
    private CameraSource cam;
    private static final int REQUEST_CAM_CODE = 201;
    private TextView barcode_txt;
    private String barcode_data;

    private final ActivityResultLauncher<ScanOptions> barcode_scanner = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null){
                    Toast.makeText(barcodeScanner.this, "Cancelled", Toast.LENGTH_LONG).show();
                        } else {
                    Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    barcode_txt = findViewById(R.id.barcode_text);
                    barcode_txt.setText(result.getContents());
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
    /* USES DEPRECIATED METHOD OF BARCODE SCANNER, unsure if this one works lol

    public void startBarcodeScanner(){
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.ALL_FORMATS
                ).build();
        //starts the detector
        IntentIntegrator integrator = new IntentIntegrator(barcodeScanner.this);
        integrator.setCaptureActivity(CaptureActivity.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            Toast.makeText(getApplicationContext(), result.getContents(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "No results", Toast.LENGTH_SHORT).show();
        }
    }
    */
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}