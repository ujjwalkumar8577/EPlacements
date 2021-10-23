package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        binding.buttonLogin.setOnClickListener(view -> {
            String regno = binding.editTextRegistration.getText().toString();
            String password = binding.editTextPassword.getText().toString();
            if(!regno.equals("")) {
                if(!password.equals("")) {
                    loginStudent(regno, password);
                }
                else {
                    binding.editTextPassword.setError("Enter password");
                }
            }
            else {
                binding.editTextRegistration.setError("Enter registration no.");
            }
        });

        binding.textViewHelp.setOnClickListener(view -> {
            Intent in = new Intent();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), HelpActivity.class);
            startActivity(in);
        });

        binding.textViewAdmin.setOnClickListener(view -> {
            Intent in = new Intent();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), AdminLoginActivity.class);
            startActivity(in);
        });

        binding.textViewSignup.setOnClickListener(view -> {
            Intent in = new Intent();
            in.setAction(Intent.ACTION_VIEW);
            in.setClass(getApplicationContext(), SignupActivity.class);
            startActivity(in);
        });
    }

    private void loginStudent(String regno, String password) {
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url) + "student/loginStudent";
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
                            binding.animationViewLoading.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();

                            Intent in = new Intent();
                            in.setAction(Intent.ACTION_VIEW);
                            in.setClass(getApplicationContext(), HomeActivity.class);
                            startActivity(in);
                            finish();
                        }
                        else
                            Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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