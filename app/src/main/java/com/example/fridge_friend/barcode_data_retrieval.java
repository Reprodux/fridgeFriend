package com.example.fridge_friend;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class barcode_data_retrieval extends AsyncTask<String, Void, String> {

    private final String TAG = "barcode_to_data Class";

    // Replace this with your actual API endpoint
    private final String API_LINK = "https://world.openfoodfacts.org/api/v2/product/";
    private final String sample_barcode = "060410010983";

    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        String barcode = params[0];
        String urlString = API_LINK + barcode + ".json";

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching product data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //TODO: use the resulting JSON to parse the data and return it BACK to the activity/function calling it to display
        if (result != null) {
            try {
                // Parse the JSON response
                JSONObject itemJSON = new JSONObject(result);

                // Log the outer keys
                Log.d(TAG, "Outer Keys: " + itemJSON.keys());

                Iterator<String> keysIterator = itemJSON.keys();

                // Iterate over the keys and log each one
                while (keysIterator.hasNext()) {
                    String key = keysIterator.next();
                    Log.d(TAG, "Outer Key: " + key);
                }

                String productCode = itemJSON.getString("code");
                Log.d(TAG, "code: " + productCode);
                String productName = itemJSON.getJSONObject("product").getString("product_name");
                Log.d(TAG, "name: " +productName);
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON", e);
            }


        } else {
            Log.e(TAG, "Error getting outer keys");
        }

    }
}