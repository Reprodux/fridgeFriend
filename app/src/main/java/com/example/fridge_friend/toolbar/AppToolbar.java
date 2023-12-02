package com.example.fridge_friend.toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fridge_friend.LoginActivity;
import com.example.fridge_friend.MainActivity;
import com.example.fridge_friend.R;
import com.example.fridge_friend.UserSettingActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity with added toolbar
 * Extend instead of {@link AppCompatActivity} to add toolbar to activity
 */
public abstract class AppToolbar extends AppCompatActivity implements ToolBarInterface {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Add the app toolbar to the activity
     * DO NOT OVERRIDE IN SUBCLASS!!
     * Instead override addMenuIcons(Menu)
     * @see ToolBarInterface#addMenuItems(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        addMenuItems(menu);
        return true;
    }

    /**
     * Handle items being selected on the app toolbar
     * DO NOT OVERRIDE IN SUBCLASS!!
     * Instead override handleMenuItemSelected(MenuItem)
     * @see ToolBarInterface#handleMenuItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_settings){
            Intent intent = new Intent(this, UserSettingActivity.class);
            startActivity(intent);
        }else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            Runtime.getRuntime().exit(0);
        } else if (id == R.id.action_about) {
            about();
        }
        return handleMenuItemSelected(item);
    }

    public void about() {
        // TODO: Implement proper about

    }

}