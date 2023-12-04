package com.example.fridge_friend;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

/**
 * The type Item detail activity.
 */
public class ItemDetailActivity extends AppToolbar {

    /**
     * The constant EXTRA_ITEM_ID.
     */
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private TextView textViewWelcomeUser;
    private TextView textViewItemOwner;
    private TextView textViewItemQuantity;
    private TextView textViewExpiryDate;
    private Button delete_btn;
    ProgressDialog progressPopup;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        delete_btn = findViewById(R.id.item_delete_btn);
        // Bind the TextViews from the layout
        textViewWelcomeUser = findViewById(R.id.textViewWelcomeUser);
        textViewItemOwner = findViewById(R.id.textViewItemOwner);
        textViewItemQuantity = findViewById(R.id.textViewItemQuantity);
        textViewExpiryDate = findViewById(R.id.textViewExpiryDate);

        progressPopup = new ProgressDialog(this);
        progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressPopup.setMax(100);
        progressPopup.setCancelable(false);
        Intent intent = getIntent();
        Long amount = intent.getLongExtra("QUANTITY",0);
        String owner = intent.getStringExtra("OWNER");
        String expiry = intent.getStringExtra("EXPIRATION");
        String p_name = intent.getStringExtra("EXTRA_ITEM_NAME");
        int id = intent.getIntExtra("ID", 0);
        String uid = FirebaseAuth.getInstance().getUid();
        Log.i(TAG, "uid: " + uid);
        p_name = "<b>" + p_name +"</b>";
        Log.i(TAG, owner);
        owner = "<b>Owner: </b>" + owner;

        if (expiry.matches("")) {

            expiry = "N/A";
        }
        expiry = "<b>Expiry: </b>" + expiry;
        String str_amount = "<b>Quantity: </b>" + amount;
        setResult(Activity.RESULT_OK,
                new Intent().putExtra("EXTRA_FRIDGE_NAME", intent.getStringExtra("EXTRA_FRIDGE_NAME")));

        //set delete_btn to delete current item from current fridge
        Log.i(TAG, textViewItemOwner.getText().toString());

        delete_btn.setOnClickListener(v -> {
            //if the item owner text view is not the owner, then write a toast saying that this isnt the current owners item

                Toast.makeText(this, "This is not your item to delete!", Toast.LENGTH_SHORT).show();

                progressPopup.show();
                Toast.makeText(this, "Removing item...", Toast.LENGTH_SHORT).show();
                Log.i(TAG, intent.getStringExtra("EXTRA_FRIDGE_NAME"));
                Log.i(TAG, intent.getStringExtra("ID"));
                Database.removeItem(this, intent.getStringExtra("EXTRA_FRIDGE_NAME"), intent.getStringExtra("ID"), new RemoveListener(this));
            });


        textViewWelcomeUser.setText(Html.fromHtml(p_name));
        textViewItemOwner.setText(Html.fromHtml(owner));
        textViewItemQuantity.setText(Html.fromHtml(str_amount));
        textViewExpiryDate.setText(Html.fromHtml(expiry));
    }

    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ItemDetailActivity.this));
        alert_builder.setTitle(R.string.item_detail_title).setMessage(R.string.itemDetailAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");
        }).show();
    }
    private class RemoveListener implements OperationCompleteListener {

        private final ItemDetailActivity activity;

        /**
         * Instantiates a new Remove listener.
         *
         * @param activity the activity
         */
        RemoveListener(ItemDetailActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("ItemDetailsActivity", "Remove Error!", e);
            progressPopup.dismiss();
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_removing_user);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Intent intent = new Intent();
            activity.setResult(Activity.RESULT_OK, intent);
            Log.i(TAG, "deleted obj");
            progressPopup.dismiss();
            activity.finish();

        }
    }
    @Override
    public void onResume(){
        super.onResume();
        String uid = FirebaseAuth.getInstance().getUid();
        Log.i(TAG, "uid: " + uid);
    }

}