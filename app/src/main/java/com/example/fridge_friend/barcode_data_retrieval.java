package com.example.fridge_friend;


import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The type Barcode data retrieval.
 */
public class barcode_data_retrieval extends AsyncTask<String, Void, String> {


    private final String TAG = "barcode_to_data Class";
    private final String API_LINK = "https://world.openfoodfacts.org/api/v2/product/";
    //replace variable barcode with sample_barcode for demoing(maybe)
    private final String sample_barcode = "060410010983";

    /**
     * The interface Response.
     */
    public interface response {
        /**
         * Process finish.
         *
         * @param product_name       the product name
         * @param product_code       the product code
         * @param product_categories the product categories
         * @param brands             the brands
         * @param product_facts      the product facts
         */
        void processFinish(String product_name, String product_code, List product_categories,
                           List brands, HashMap product_facts);
    }

    /**
     * The Jso nresponse.
     */
    public response JSONresponse = null;

    /**
     * Instantiates a new Barcode data retrieval.
     *
     * @param JSONresponse the jso nresponse
     */
    public barcode_data_retrieval(response JSONresponse){
        this.JSONresponse = JSONresponse;
    }

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
        //TODO: return the variables BACK to the activity/function calling it to display
        if (result != null) {
            try {
                // Return Variables
                Boolean product_valid = false;
                String product_name = "";
                String product_code = "";
                List<String> product_categories = new ArrayList<String>();
                List<String> brands = new ArrayList<String>();
                HashMap<String, String> product_facts = new HashMap<String, String>();

                // Wanted Nutrients Array filled with the following:
                // salt_100g, sodium_100g, sugars_100g, fat_100g, saturated-fat_100g, proteins_100g, energy-kcal_100g
                List<String> wanted_nutrients = new ArrayList<String>();
                wanted_nutrients.add("salt_100g");
                wanted_nutrients.add("sodium_100g");
                wanted_nutrients.add("sugars_100g");
                wanted_nutrients.add("fat_100g");
                wanted_nutrients.add("saturated-fat_100g");
                wanted_nutrients.add("proteins_100g");
                wanted_nutrients.add("energy-kcal_100g");

                // Parse the JSON response
                JSONObject itemJSON = new JSONObject(result);

                String status_verbose = itemJSON.getString("status_verbose");
                if (status_verbose.equals("product found")) {
                    product_valid = true;
                } else {
                    product_valid = false;
                }

                if (itemJSON.has("code")) {
                    product_code = itemJSON.getString("code");
                }

                if (product_valid) {
                    // Get "product" object
                    JSONObject productJSON = itemJSON.getJSONObject("product");

                    // Check if product is locally sourced + Set product name accordingly
                    if (productJSON.has("user_data_origin")) { // sometimes is found in "traces_from_user" instead
                        String user_data_origin = productJSON.getString("user_data_origin");
                        if (user_data_origin.contains("(en)")) {
                            product_name = productJSON.getString("product_name");
                        } else {
                            product_name = productJSON.getString("abbreviated_product_name");
                        }

                        if (product_name.equals("")) {
                            product_name = productJSON.getString("product_name_fr");
                        }
                    } else { // Sometimes the json will not contain "user_data_origin"
                        if (productJSON.has("product_name")) {
                            product_name = productJSON.getString("product_name");
                        } else {
                            product_name = productJSON.getString("abbreviated_product_name");
                        }
                    }

                    // Get "categories" array
                    if (productJSON.has("categories_hierarchy")) {
                        JSONArray categoriesJSON = productJSON.getJSONArray("categories_hierarchy");
                        for (int i = 0; i < categoriesJSON.length(); i++) {
                            String category = categoriesJSON.getString(i);
                            category = category.replace("en:", "");
                            category = category.replace("-", " ");
                            product_categories.add(category);
                        }
                    }

                    // Get "brands" array
                    if (productJSON.has("brands")) {
                        try{
                            JSONArray brandsJSON = productJSON.getJSONArray("brands");
                            for (int i = 0; i < brandsJSON.length(); i++) {
                                String brand = brandsJSON.getString(i);
                                brands.add(brand);
                            }
                        } catch (Exception ignore){

                        }finally {
                            String brandsJSON = productJSON.getString("brands");
                            for (int i = 0; i < brandsJSON.length(); i++) {
                                String brand = brandsJSON;
                                brands.add(brand);
                            }
                        }


                    }

                    // Get "nutriments" object
                    if (productJSON.has("nutriments")) {
                        JSONObject nutrimentsJSON = productJSON.getJSONObject("nutriments");

                        // Iterate through wanted nutrients and add them to product_facts
                        for (int i = 0; i < wanted_nutrients.size(); i++) {
                            String nutrient = wanted_nutrients.get(i);
                            if (nutrimentsJSON.has(nutrient)) {
                                String nutrient_value = nutrimentsJSON.getString(nutrient);
                                product_facts.put(nutrient, nutrient_value);
                            }
                        }
                    }
                }

                // return the parsed data
                JSONresponse.processFinish(product_name, product_code,
                        product_categories, brands, product_facts);

            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON", e);
            }


        } else {
            Log.e(TAG, "Error getting outer keys");
        }

    }
}