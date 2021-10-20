package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivitySignupBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        binding.buttonSignup.setOnClickListener(view -> {
            String regno = binding.editTextRegistration.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            if(!regno.equals("")) {
                if(!password.equals("")) {
                    signupStudent(regno, password);
                }
                else {
                    binding.editTextPassword.setError("Enter password");
                }
            }
            else {
                binding.editTextRegistration.setError("Enter registration no.");
            }
        });

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.textViewHelp.setOnClickListener(view -> {
            Intent in = new Intent();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), HelpActivity.class);
            startActivity(in);
        });
    }

    private void signupStudent(String regno, String password) {
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url) + "student/registerStudent";
        JSONObject postData = new JSONObject();
        try {
            postData.put("reg_no", regno);
            postData.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            String name = response.getJSONObject("user").getString("name");
                            String regno1 = response.getJSONObject("user").getString("reg_no");
                            String course = response.getJSONObject("user").getString("course");
                            String branch = response.getJSONObject("user").getString("branch");
                            String status = response.getJSONObject("user").getString("status");
                            String credits = response.getJSONObject("user").getString("credits");
                            String token = response.getString("token");
                            user.edit().putString("name", name).apply();
                            user.edit().putString("id", regno1).apply();
                            user.edit().putString("course", course).apply();
                            user.edit().putString("branch", branch).apply();
                            user.edit().putString("status", status).apply();
                            user.edit().putString("credits", credits).apply();
                            user.edit().putString("token", token).apply();
                            user.edit().putString("type", "student").apply();
                            binding.animationViewLoading.pauseAnimation();
                            Toast.makeText(SignupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                            Intent in = new Intent();
                            in.setAction(Intent.ACTION_VIEW);
                            in.setClass(getApplicationContext(), HomeActivity.class);
                            startActivity(in);
                            finish();
                        }
                        binding.animationViewLoading.pauseAnimation();
                        Toast.makeText(SignupActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/json");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}