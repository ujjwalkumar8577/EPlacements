package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.databinding.ActivityViewPdfBinding;

public class ViewPdfActivity extends AppCompatActivity {

    private ActivityViewPdfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String BASE_URL = "https://drive.google.com/viewerng/viewer?embedded=true&url=";
        String url = getIntent().getStringExtra("url");

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(BASE_URL+url);
    }
}