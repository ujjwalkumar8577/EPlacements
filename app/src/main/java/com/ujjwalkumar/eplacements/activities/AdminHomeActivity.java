package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminHomeBinding;

public class AdminHomeActivity extends AppCompatActivity {

    private ActivityAdminHomeBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        FirebaseMessaging.getInstance().subscribeToTopic("allAdmins");

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, AdminMyAccountActivity.class));
        });

        binding.imageViewNotice.setOnClickListener(view -> {
            startActivity(new Intent(this, NoticesActivity.class));
        });

        binding.layoutMenu1.setOnClickListener(view -> {
            startActivity(new Intent(this, AdminAddCompanyActivity.class));
        });

        binding.layoutMenu2.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });

        binding.layoutMenu3.setOnClickListener(view -> {
            startActivity(new Intent(this, AdminManageStudentsActivity.class));
        });

        binding.layoutMenu4.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });

        binding.layoutMenu5.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });

        binding.layoutMenu6.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.textViewRole.setText(user.getString("course", ""));
    }
}