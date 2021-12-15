package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityHelpBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {

    private ActivityHelpBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        
        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
        
        binding.buttonSend.setOnClickListener(view -> {
            String message = binding.editTextMessage.getText().toString();
            sendHelp(message);
        });
    }
    
    private void sendHelp(String message) {
        String url = getString(R.string.base_url) + "student/addGrievance";
        JSONObject postData = new JSONObject();
        try {
            postData.put("message", message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            binding.editTextMessage.setText("");
                            Toast.makeText(HelpActivity.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(HelpActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(HelpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}