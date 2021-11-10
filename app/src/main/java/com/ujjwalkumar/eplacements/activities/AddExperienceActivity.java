package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.databinding.ActivityAddExperienceBinding;

public class AddExperienceActivity extends AppCompatActivity {

    private ActivityAddExperienceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

    }
}