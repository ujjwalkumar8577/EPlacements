package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.adapters.RegisteredCompanyAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityRegisteredCompaniesBinding;
import com.ujjwalkumar.eplacements.models.RegisteredCompany;
import com.ujjwalkumar.eplacements.utilities.EPlacementsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisteredCompaniesActivity extends AppCompatActivity {

    private ArrayList<RegisteredCompany> al;
    private ActivityRegisteredCompaniesBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredCompaniesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EPlacementsUtil.checkInternetConnection(this);

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void showInformation() {
        startLoading();
        String url = getString(R.string.base_url) + "student/getRegisteredCompanies";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray companies = response.getJSONArray("companies");
                            al = new ArrayList<>();
                            for(int i=0; i< companies.length(); i++) {
                                JSONObject obj = companies.getJSONObject(i).getJSONObject("company");
                                long timestamp = companies.getJSONObject(i).getLong("timestamp");
                                RegisteredCompany registeredCompany = new RegisteredCompany(obj.getString("_id"), obj.getString("name"), obj.getString("job_profile"), obj.getDouble("ctc"), timestamp);
                                al.add(registeredCompany);
                            }

                            RegisteredCompanyAdapter adapter = new RegisteredCompanyAdapter(RegisteredCompaniesActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredCompaniesActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(RegisteredCompaniesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(RegisteredCompaniesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void startLoading() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        binding.shimmerFrameLayout.startShimmer();
    }

    private void stopLoading() {
        binding.shimmerFrameLayout.stopShimmer();
        binding.shimmerFrameLayout.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }
}