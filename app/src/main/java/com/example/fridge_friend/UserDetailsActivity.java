package com.example.fridge_friend;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fridge_friend.database.Database;
import com.example.fridge_friend.database.listener.OperationCompleteListener;
import com.example.fridge_friend.toolbar.AppToolbar;
import com.google.firebase.auth.FirebaseAuth;

/** @noinspection deprecation*/
public class UserDetailsActivity extends AppToolbar {

    private String fridgeName;
    private String userId;
    private ProgressDialog progressPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        if(FirebaseAuth.getInstance().getUid() == null || getIntent() == null || !getIntent().hasExtra("fridgeName") || !getIntent().hasExtra("userId") || !getIntent().hasExtra("userName")) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        fridgeName = getIntent().getStringExtra("fridgeName");
        userId = getIntent().getStringExtra("userId");
        if (fridgeName == null || fridgeName.length() == 0 || userId == null ||  userId.length() == 0) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.userDetailsFrame, UserDetailsFragment.class, getIntent().getExtras()).setReorderingAllowed(true).commit();
        getSupportFragmentManager().setFragmentResultListener("buttonClick", this, (requestKey, result) -> {
            fridgeName = result.getString("fridgeName");
            userId = result.getString("userId");
            if (userId != null && userId.equals(FirebaseAuth.getInstance().getUid())) {
                progressPopup.setMessage(getString(R.string.leaving_fridge));
                progressPopup.show();
                Database.leaveFridge(this, fridgeName, new LeaveListener(this));
            } else {
                progressPopup.setMessage(getString(R.string.removing_user));
                progressPopup.show();
                Database.removeFromFridge(this, userId, fridgeName, new RemoveListener(this));
            }
        });

        progressPopup = new ProgressDialog(this);
        progressPopup.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressPopup.setMax(100);
        progressPopup.setCancelable(false);

    }

    private class LeaveListener implements OperationCompleteListener {

        private final UserDetailsActivity activity;

        LeaveListener(UserDetailsActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("UserDetailsActivity", "Leaving Error!", e);
            activity.progressPopup.dismiss();
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_leaving_fridge);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Intent intent = new Intent();
            intent.putExtra("userId", activity.userId);
            intent.putExtra("operation", "leave");
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    private class RemoveListener implements OperationCompleteListener {

        private final UserDetailsActivity activity;

        RemoveListener(UserDetailsActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onFailure(Exception e) {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            Log.e("UserDetailsActivity", "Remove Error!", e);
            activity.progressPopup.dismiss();
            AlertDialog.Builder errorAlert = new AlertDialog.Builder(activity);
            errorAlert.setMessage(R.string.error_removing_user);
            errorAlert.setPositiveButton(android.R.string.ok, (dialog, which) -> finish());
            errorAlert.show();
        }

        @Override
        public void onCanceled() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Toast.makeText(activity, R.string.cancelled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess() {
            if (activity.isDestroyed() || activity.isFinishing()) return;
            activity.progressPopup.dismiss();
            Intent intent = new Intent();
            intent.putExtra("userId", activity.userId);
            intent.putExtra("operation", "remove");
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

}