package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button open_cam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        open_cam = findViewById(R.id.camera_access_btn);
        open_cam.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), barcodeScanner.class);
            MainActivity.this.startActivity(intent);

        });
    }

    public void checkData(View V){
        FirebaseAuth.getInstance().signOut();
        Log.i(TAG, "Signed out");
        Toast.makeText(MainActivity.this, "Signed out",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}