package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, MyAccountActivity.class));
        });

        binding.imageViewNotice.setOnClickListener(view -> {
            startActivity(new Intent(this, NoticesActivity.class));
        });

        binding.layoutMenu1.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });

        binding.layoutMenu2.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisteredCompaniesActivity.class));
        });

        binding.layoutMenu3.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });

        binding.layoutMenu4.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisteredCompaniesActivity.class));
        });

        binding.layoutMenu5.setOnClickListener(view -> {
            startActivity(new Intent(this, ContactsActivity.class));
        });

        binding.layoutMenu6.setOnClickListener(view -> {
            startActivity(new Intent(this, HelpActivity.class));
        });
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.textViewDegreeCourse.setText(user.getString("course", "") + " - " + user.getString("branch", ""));
        binding.textViewCredits.setText(user.getString("credits", ""));
        binding.textViewStatus.setText(getStatus(user.getString("status", "")));
        if(user.getString("status", "").equals("registered") || user.getString("status", "").equals("unverified"))
            binding.imageViewVerified.setVisibility(View.GONE);
        else
            binding.imageViewVerified.setVisibility(View.VISIBLE);
    }

    private String getStatus(String status) {
        if(status.equals("placed"))
            return "Offered";
        else
            return "Not Offered";
    }
}