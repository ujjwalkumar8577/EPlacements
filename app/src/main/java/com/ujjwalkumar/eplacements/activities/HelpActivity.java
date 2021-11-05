package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {

    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
        
        binding.buttonSend.setOnClickListener(view -> {
            String message = binding.editTextMessage.getText().toString();
            sendHelp(message);
        });
    }
    
    private void sendHelp(String message) {
        Toast.makeText(HelpActivity.this, "Sent successfully", Toast.LENGTH_SHORT).show();
    }
}