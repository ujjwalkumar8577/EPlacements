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

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

//    private void loadRecyclerView() {
//        al = new ArrayList<>();
//        al.add(new RegisteredCompany("1", "Google Inc", "SDE I", "40 LPA", "21/10/2021 11:59 PM"));
//        al.add(new RegisteredCompany("2","Amazon", "SDE I", "32 LPA", "10/10/2021 11:59 PM"));
//        al.add(new RegisteredCompany("3","Myntra", "Business Analyst", "12 LPA", "26/10/2021 11:59 PM"));
//        al.add(new RegisteredCompany("4","Facebook", "Technology Analyst", "18 LPA", "18/10/2021 11:59 PM"));
//        al.add(new RegisteredCompany("5","Tata Motors", "Junior Engineer", "9 LPA", "29/10/2021 11:59 PM"));
//        al.add(new RegisteredCompany("6","Trilogy Innovations", "Software Developer", "30.5 LPA", "12/10/2021 11:59 PM"));
//
//        RegisteredCompanyAdapter adapter = new RegisteredCompanyAdapter(this, al);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        binding.recyclerView.setAdapter(adapter);
//    }

    private void showInformation() {
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url) + "student/getRegisteredCompanies";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray notices = response.getJSONArray("company");
                            al = new ArrayList<>();
                            for(int i=0; i< notices.length(); i++) {
                                JSONObject obj = notices.getJSONObject(i);

                                RegisteredCompany registeredCompany = new RegisteredCompany(obj.getString("_id"), obj.getString("name"), obj.getString("job_profile"), obj.getDouble("ctc"), obj.getLong("timestamp"));
                                al.add(registeredCompany);
                            }

                            RegisteredCompanyAdapter adapter = new RegisteredCompanyAdapter(RegisteredCompaniesActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(RegisteredCompaniesActivity.this));
                            binding.recyclerView.setAdapter(adapter);

                            binding.animationViewLoading.pauseAnimation();
                            binding.animationViewLoading.setVisibility(View.GONE);
                        }
                        binding.animationViewLoading.pauseAnimation();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
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
}