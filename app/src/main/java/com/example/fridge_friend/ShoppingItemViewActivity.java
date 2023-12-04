package com.example.fridge_friend;

import static android.app.ProgressDialog.show;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.Item;
import com.example.fridge_friend.database.listener.BaseListener;
import com.example.fridge_friend.database.listener.FridgeListListener;
import com.example.fridge_friend.database.listener.ItemListener;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.database.listener.UserNameListener;
import com.example.fridge_friend.database.local.ShoppingCartItem;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Shopping item view activity.
 */
public abstract class ShoppingItemViewActivity extends AppToolbar implements barcode_data_retrieval.response, FridgeListListener, OperationCompleteListener {

    private TextView textViewWelcomeUser;
    private TextView textViewItemName;
    private TextView textViewItem;
    private int selectedFridge;
    private TextView textViewExpiryDate;
    private Button addToFridge;
    private String fridgeChosen;
    boolean add_req;
    private String p_name;
    private String date_str;
    private int amount;
    private String owner;
    private Item item;



    private ProgressBar item_load_bar;
    private ProgressDialog progressPopup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_barcode_data);
        int selectedFridge;
        //assume that intent.getStringExtra("barcode_str") should return the 13 digit barcode
        Intent intent = getIntent();
        String upc = intent.getStringExtra("upc");
        textViewItem = findViewById(R.id.itemData);
        item_load_bar = findViewById(R.id.item_load_bar);
        item_load_bar.setVisibility(View.VISIBLE);
        item_load_bar.setProgress(50);
        addToFridge = findViewById(R.id.addToFridge);
        progressPopup = new ProgressDialog(this);
        progressPopup.setMessage(getString(R.string.retrieving_data)); // msg dialog
        progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressPopup.setCancelable(false);


        //Toast.makeText(barcodeScanner.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
        item_load_bar.setProgress(75);

        Log.d(TAG, "ITEM ID DETAILS " + upc);
        textViewItemName = findViewById(R.id.itemName);
        new barcode_data_retrieval(this).execute(upc);


        addToFridge.setOnClickListener(view -> {
            progressPopup.show();
            Database.listFridges(this, this);


        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void about() {
        android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder((ShoppingItemViewActivity.this));
        alert_builder.setTitle(R.string.shopping_item_detail_title).setMessage(R.string.shoppingItemDetailAbout);
        alert_builder.setPositiveButton(R.string.ok, (dialogInterface, id) -> {
            Log.i(TAG, "User clicked about");

        }).show();
    }

    /**
     * Capitialize words string.
     *
     * @param word the word
     * @return the string
     */
    public String capitializeWords(String word) {
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
            product_info += "<b>Name: </b>" + product_name + "<br>";
            p_name = product_name;

            if (product_code != null) {
                product_info += "<b>Code: </b>" + product_code + "<br>";
            }

            if (product_categories != null) {
                product_info += "<b>Categories: </b>" + "<br>";

                // Get shortest category
                int shortest = 100;
                int shortest_index = 0;
                for (Object category : product_categories) {
                    if (category.toString().length() < shortest) {
                        shortest = category.toString().length();
                        shortest_index = product_categories.indexOf(category);
                    }
                }

                product_info += "   " + capitializeWords(product_categories.get(shortest_index).toString()) + "<br>";
            }

            if (brands != null) {
                product_info += "<b>Brands: </b>" + "<br>";
                for (Object brand : brands) {
                    product_info += "   " + capitializeWords(brand.toString()) + "<br>";
                    break;
                }
            }

            if (product_facts != null) {
                product_info += "<b>Facts (per 100g): </b>" + "<br>";
                for (Object key : product_facts.keySet()) {
                    String cleanKey = key.toString().replaceAll("_100g", "");
                    cleanKey = cleanKey.replaceAll("-", " ");

                    product_info += "   <b>" + capitializeWords(cleanKey) + ": </b>" + Math.round(Float.parseFloat(product_facts.get(key).toString())) + "<br>";
                }
            }
            item_load_bar.setProgress(100);
            item_load_bar.setVisibility(View.INVISIBLE);
        }

        textViewItem.setGravity(0);
        textViewItem.setText(Html.fromHtml(product_info));
        assert product_code != null;
        ShoppingCartItem item = new ShoppingCartItem(product_name, 1, product_code);
        long id = item.getId();
        String upc = item.getUPC();
        Log.i(TAG, "Stored item id: " + String.valueOf(id));
        Log.i(TAG, "Stored item id: " + String.valueOf(upc));
        Log.i(TAG, "processFinish in barcode scanner activity run");
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onCanceled() {

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onListResult(List<String> result) {
        progressPopup.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingItemViewActivity.this);
        builder.setTitle(getString(R.string.add_to_fridge));
        String[] choices = result.toArray(new String[result.size()]);
        final EditText input = new EditText(this);
        EditText date = new EditText(this);
        final TextView select_date = new TextView(this);
        final TextView quantity = new TextView(this);
        Log.i(TAG, "onListResult");
        //add number input to builder called quantity

        TextWatcher tw = new TextWatcher() {
            private String cur = "";
            private String format_date = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                if (!str.toString().equals(cur)) {
                    String clean = str.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = cur.replaceAll("[^\\d.]|\\.", "");

                    int len = clean.length();
                    int selection = len;
                    for (int i = 2; i <= len && i < 6; i += 2) {
                        selection++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) selection--;

                    if (clean.length() < 8){
                        clean = clean + format_date .substring(clean.length());
                    }else{
                        //Ensures proper inputting of numbers
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);

                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    selection = selection < 0 ? 0 : selection;
                    cur = clean;
                    date.setText(cur);
                    date.setSelection(selection < cur.length() ? selection : cur.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };
        date.addTextChangedListener(tw);
        input.setSingleLine();
        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams txtparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams selectDateParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams dateParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.topdialog_margin);

        txtparams.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        txtparams.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        selectDateParam.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        selectDateParam.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        selectDateParam.topMargin = getResources().getDimensionPixelSize(R.dimen.sd_topdialog_margin);

        dateParam.topMargin = getResources().getDimensionPixelSize(R.dimen.d_topdialog_margin);
        dateParam.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        dateParam.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        input.setLayoutParams(params);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.setText("1");
        select_date.setLayoutParams(selectDateParam);
        select_date.setText("Enter the Expiry Date DD/MM/YYYY");
        date.setLayoutParams(dateParam);
        date.setRawInputType(Configuration.KEYBOARD_12KEY);
        date.setHint("DD/MM/YYYY");
        quantity.setLayoutParams(txtparams);
        quantity.setText("Enter the Quantity");

        container.addView(quantity);
        container.addView(input);
        container.addView(select_date);
        container.addView(date);

        builder.setView(container);
        builder.setPositiveButton(getString(R.string.add_to_fridge), (dialog, which) -> {
            add_req = true;
            Log.i(TAG, "Request to add item to fridge " + choices[selectedFridge]);
            //checks if the date inputted is of valid format, and let the user know
            if (isValidDate(date.getText().toString())) {
                fridgeChosen = choices[selectedFridge];
                date_str = date.getText().toString();
                amount = Integer.valueOf(input.getText().toString());
                Log.i(TAG, "fridge chosen " + fridgeChosen);
                String uid = FirebaseAuth.getInstance().getUid();
                Database.getUserName(this, uid, new LoadingListener(this));




            } else {
                //highlight date box and tell user that valid date format is needed
                date.setBackgroundColor(R.color.off_red);
                Toast.makeText(this, "Invalid Date Format", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Invalid Date Format");
                add_req = false;
                //do not exit the builder

            }


        }).setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
            Log.i(TAG, "Cancelled add to Fridge");
        }).setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFridge = which;
                Log.i(TAG, "selected" + choices[selectedFridge]);

            }
        });
        builder.show();


    }

    //function to verify if the date inputted is of valid format DD/MM/YYYY

    public boolean isValidDate(String date) {
        //date is of format DD/MM/YYYY
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            format.parse(date);
        }
        catch (ParseException e) {
            return false;
        }
        return true;

    }

    private class LoadingListener implements UserNameListener {

        private final ShoppingItemViewActivity activity;

        /**
         * Instantiates a new Loading listener.
         *
         * @param activity the activity
         */
        LoadingListener( ShoppingItemViewActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onResult(String name, String id) {
            progressPopup.show();
            owner = name;
            item = new Item(p_name, Long.valueOf(amount), date_str, owner);
            Database.addItem(activity, fridgeChosen, item, new AddItemListener(activity));
            progressPopup.dismiss();
            Toast.makeText(activity, "Added " + amount+ " " + p_name, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("ShoppingItemViewActivity", "Loading Error!", e);
            androidx.appcompat.app.AlertDialog.Builder errorAlert = new androidx.appcompat.app.AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_loading_name);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

    }

    private class AddItemListener implements OperationCompleteListener {
        private final ShoppingItemViewActivity activity;

        /**
         * Instantiates a new Saving listener.
         *
         * @param activity the activity
         */
        AddItemListener(ShoppingItemViewActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onSuccess() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            progressPopup.dismiss();
            Toast.makeText(activity, R.string.changes_saved, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("Item Adding", "Item Adding Error!", e);
            progressPopup.dismiss();
            androidx.appcompat.app.AlertDialog.Builder errorAlert = new androidx.appcompat.app.AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_saving_changes);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }
    }






}

