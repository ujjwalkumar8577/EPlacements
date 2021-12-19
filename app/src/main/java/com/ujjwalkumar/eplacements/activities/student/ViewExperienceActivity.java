package com.ujjwalkumar.eplacements.activities.student;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.databinding.ActivityViewExperienceBinding;

public class ViewExperienceActivity extends AppCompatActivity {

    private ActivityViewExperienceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }
}