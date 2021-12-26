package com.ujjwalkumar.eplacements.activities.student;

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
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.adapters.ExperienceAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityViewExperienceBinding;
import com.ujjwalkumar.eplacements.models.Experience;
import com.ujjwalkumar.eplacements.utilities.EPlacementsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewExperienceActivity extends AppCompatActivity {

    private ArrayList<Experience> al;
    private ActivityViewExperienceBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EPlacementsUtil.checkInternetConnection(this);

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        showInformation();
    }

    private void showInformation() {
        startLoading();
        String url = getString(R.string.base_url) + "student/getExperience";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray experiences = response.getJSONArray("experience");
                            al = new ArrayList<>();
                            for(int i=0; i< experiences.length(); i++) {
                                JSONObject obj = experiences.getJSONObject(i);
                                Gson gson = new Gson();
                                Experience experience = gson.fromJson(obj.toString(), Experience.class);

                                al.add(experience);
                            }

                            ExperienceAdapter adapter = new ExperienceAdapter(ViewExperienceActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(ViewExperienceActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(ViewExperienceActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(ViewExperienceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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