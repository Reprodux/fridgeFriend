package com.example.fridge_friend;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserDetailsFragment extends Fragment {

    private String fridgeName;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fridgeName = requireArguments().getString("fridgeName");
        userId = requireArguments().getString("userId");
        String userName = requireArguments().getString("userName");
        View view =  inflater.inflate(R.layout.fragment_user_details, container, false);
        TextView userNameView = view.findViewById(R.id.userNameView);
        TextView fridgeNameView = view.findViewById(R.id.fridgeView);
        Button leaveButton = view.findViewById(R.id.leaveButton);
        if (userNameView != null) {
            userNameView.setText(getString(R.string.username_prefix, userName));
        }
        if (fridgeNameView != null) {
            fridgeNameView.setText(getString(R.string.fridge_prefix, fridgeName));
        }
        if (leaveButton != null) {
            if (userId.equals(FirebaseAuth.getInstance().getUid())) {
                leaveButton.setText(R.string.leave_fridge);
            } else {
                leaveButton.setText(R.string.remove_user);
            }
            leaveButton.setOnClickListener(this::onButtonClick);
            System.out.println(leaveButton.getText());
        }
        return view;
    }

    private void onButtonClick(View v) {
        Bundle result = new Bundle();
        result.putString("fridgeName", fridgeName);
        result.putString("userId", userId);
        getParentFragmentManager().setFragmentResult("buttonClick", result);
    }

}