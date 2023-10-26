package com.example.fridge_friend;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    Button login_request_btn;
    EditText login_email_input;
    EditText pass_input;

    private FirebaseAuth mAuth;
    void loadUserData() {
        Log.i(TAG, "loadUserData()");
        String pref_file = getString(R.string.pref_file);
        SharedPreferences userPrefs = getSharedPreferences(pref_file, MODE_PRIVATE);
        String email_key = getString(R.string.pref_email);
        //gets the saved pref email from shared pref file, if its not there use defaulted value inputted
        String new_email_val = userPrefs.getString(email_key, "email@default.com");
        //loads the data into the edittext section
        ((EditText) findViewById(R.id.login_email)).setText(new_email_val);
    }
    public void saveUserData(){
        Log.i(TAG, "saveUserData()");
        //get shared pref file up
        String pref_file = getString(R.string.pref_file);
        SharedPreferences userPrefs = getSharedPreferences(pref_file, MODE_PRIVATE);
        //using the shared pref file, bring up the editor
        SharedPreferences.Editor prefEditor = userPrefs.edit();
        //clear out the editor
        prefEditor.clear();
        //get email key for key value pair
        String email_key = getString(R.string.pref_email);
        //get inputted email and save it
        String inputted_email = ((EditText)findViewById(R.id.login_email)).getText().toString();
        prefEditor.putString(email_key,inputted_email);
        //commits changes to shared pref
        prefEditor.apply();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("A2 - Login Activity");
        }

        loadUserData();
        login_request_btn = findViewById(R.id.login_request_btn);
        login_email_input = findViewById((R.id.login_email));
        pass_input = findViewById((R.id.password));
        Log.i(TAG, "Created LoginActivity");
    }



    public void checkData(View V){
        Log.i(TAG, "Checking input");
        //declare variables
        String inputted_email = login_email_input.getText().toString();
        String password = pass_input.getText().toString();
        //boolean tracker to prevent multiple popups and false positive input
        boolean valid = true;
        if(inputted_email.matches("")){
            Toast.makeText(getApplicationContext(), "You must input email to login",Toast.LENGTH_SHORT).show();
            login_email_input.setError("Email Required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputted_email).matches()) {
            Toast.makeText(getApplicationContext(), "Must be valid email format (i.e. user@email.com)",Toast.LENGTH_SHORT).show();
            login_email_input.setError("Invalid Email format");
            valid = false;
        }
        if(password.matches("")){
            if(valid){
                Toast.makeText(getApplicationContext(), "You must input password",Toast.LENGTH_SHORT).show();
            }
            pass_input.setError("Password Required");
            valid = false;
        }
        if (valid){
            mAuth.signInWithEmailAndPassword(inputted_email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
            saveEmail();
        }

    }

    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCustomToken:success");
            FirebaseUser user = mAuth.getCurrentUser();
            saveEmail();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void saveEmail(){

        saveUserData();
        //Toast.makeText(getApplicationContext(), "Logged in",Toast.LENGTH_SHORT).show();
        Intent passLogin = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(passLogin);
    }
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "Resumed LoginActivity");
    }

    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.i(TAG, "Started LoginActivity");
    }

    protected void onPause(){
        super.onPause();
        Log.i(TAG, "Paused LoginActivity");
    }

    protected void onStop(){
        super.onStop();
        Log.i(TAG, "Stopped LoginActivity");
    }

    public void register_account(View v){
        Log.i(TAG, "Checking input");
        //declare variables
        String inputted_email = login_email_input.getText().toString();
        String password = pass_input.getText().toString();
        //boolean tracker to prevent multiple popups and false positive input
        boolean valid = true;
        if(inputted_email.matches("")){
            Toast.makeText(getApplicationContext(), "You must input valid email to create account",Toast.LENGTH_SHORT).show();
            login_email_input.setError("Email Required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputted_email).matches()) {
            Toast.makeText(getApplicationContext(), "Must be valid email format (i.e. user@email.com)",Toast.LENGTH_SHORT).show();
            login_email_input.setError("Invalid Email format");
            valid = false;
        }
        if(password.matches("")){
            if(valid){
                Toast.makeText(getApplicationContext(), "You must input password",Toast.LENGTH_SHORT).show();
            }
            pass_input.setError("Password Required");
            valid = false;
        }
        if (valid){
            mAuth.createUserWithEmailAndPassword(inputted_email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });

        }

    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            Toast.makeText(LoginActivity.this, "User id: " + user.getUid(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "Failed sign-in",
                    Toast.LENGTH_SHORT).show();
        }
    }

}