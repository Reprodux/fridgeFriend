package com.example.fridge_friend;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

//this is the primary navigation hub after login, provides quick access to fridges, setting, qr code scanning
public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ImageView profileIcon = findViewById(R.id.imageViewProfileIcon);


        ImageView qrCodeImageView = findViewById(R.id.imageViewQRCode);
        ImageView fridgeImageView = findViewById(R.id.buttonFridgeInstance);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating intent to start the user setting activity
                Intent intent = new Intent(HomePage.this, UserSettingActivity.class);
                startActivity(intent);
            }
        });

        qrCodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start QRCodeActivity
                Intent qrIntent = new Intent(HomePage.this, QRCodeActivity.class);
                startActivity(qrIntent);
            }
        });

        fridgeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start FridgeActivity
                Intent fridgeIntent = new Intent(HomePage.this, FridgeListActivity.class);
                startActivity(fridgeIntent);
            }
        });
    }
}

