package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void checkData(View V){
        FirebaseAuth.getInstance().signOut();
        Log.i(TAG, "Signed out");
        Toast.makeText(MainActivity.this, "Signed out",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}