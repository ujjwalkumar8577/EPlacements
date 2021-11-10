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
import com.ujjwalkumar.eplacements.adapters.UpcomingCompanyAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityUpcomingCompaniesBinding;
import com.ujjwalkumar.eplacements.models.UpcomingCompany;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpcomingCompaniesActivity extends AppCompatActivity {

    private ArrayList<UpcomingCompany> al;
    private ActivityUpcomingCompaniesBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpcomingCompaniesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void showInformation() {
        startLoading();
        String url = getString(R.string.base_url) + "currentOpening";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray companies = response.getJSONArray("company");
                            al = new ArrayList<>();
                            for(int i=0; i< companies.length(); i++) {
                                JSONObject obj = companies.getJSONObject(i);
                                UpcomingCompany upcomingCompany = new UpcomingCompany(obj.getString("_id"), obj.getString("name"), obj.getString("job_profile"), obj.getDouble("ctc"), obj.getLong("reg_deadline"));
                                al.add(upcomingCompany);
                            }

                            UpcomingCompanyAdapter adapter = new UpcomingCompanyAdapter(UpcomingCompaniesActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(UpcomingCompaniesActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(UpcomingCompaniesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(UpcomingCompaniesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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