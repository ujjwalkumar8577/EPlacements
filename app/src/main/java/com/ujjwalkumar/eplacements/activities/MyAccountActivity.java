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
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityMyAccountBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity {

    private ActivityMyAccountBinding binding;
    private SharedPreferences user;
    private Object userObj;

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
            if(userObj!=null) {
                Gson gson = new Gson();
                String str = gson.toJson(userObj);
                Intent intent = new Intent(MyAccountActivity.this, CompanyDetailsActivity.class);
                intent.putExtra("userObj", str);
                startActivity(intent);
            }
        });
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url);
        JSONObject postData = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            userObj = response.getJSONObject("user");
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
                        Toast.makeText(MyAccountActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MyAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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