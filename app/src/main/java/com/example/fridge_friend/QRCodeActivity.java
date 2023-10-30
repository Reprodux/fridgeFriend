package com.example.fridge_friend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fridge_friend.toolbar.AppToolbar;

public class QRCodeActivity extends AppToolbar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
    }
}