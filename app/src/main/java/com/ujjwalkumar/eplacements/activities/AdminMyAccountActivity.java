package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminMyAccountBinding;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class AdminMyAccountActivity extends AppCompatActivity {

    private ActivityAdminMyAccountBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMyAccountBinding.inflate(getLayoutInflater());
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
            in.setClass(getApplicationContext(), AdminLoginActivity.class);
            startActivity(in);
            finishAffinity();
        });

        binding.textViewChangePassword.setOnClickListener(view -> {

        });

        binding.imageViewEdit.setOnClickListener(view -> {

        });
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.textViewEmail.setText(user.getString("id", ""));
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray photo = response.getJSONObject("user").getJSONObject("photo").getJSONObject("data").getJSONArray("data");
                            binding.animationViewLoading.pauseAnimation();
                            binding.animationViewLoading.setVisibility(View.GONE);
                            byte[] imageBytes = new byte[photo.length()];
                            for(int i=0; i<photo.length(); i++) {
                                imageBytes[i] = (byte)photo.getInt(i);
                            }
                            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            binding.imageViewPhoto.setImageBitmap(bmp);
                        }
                        binding.animationViewLoading.pauseAnimation();
                        Toast.makeText(AdminMyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminMyAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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