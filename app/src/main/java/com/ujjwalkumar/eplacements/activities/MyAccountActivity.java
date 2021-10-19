package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.databinding.ActivityMyAccountBinding;

public class MyAccountActivity extends AppCompatActivity {

    private ActivityMyAccountBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.buttonLogout.setOnClickListener(view -> {
            user.edit().clear().apply();
            Intent in = new Intent();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(in);
            finishAffinity();
        });

        binding.textViewChangePassword.setOnClickListener(view -> {

        });

        binding.imageViewEdit.setOnClickListener(view -> {

        });

        binding.buttonViewResume.setOnClickListener(view -> {

        });

        binding.buttonUpdateResume.setOnClickListener(view -> {

        });

        binding.buttonDetails.setOnClickListener(view -> {
            startActivity(new Intent(this, CompleteProfileActivity.class));
        });
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));

    }
}