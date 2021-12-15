package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.adapters.GrievanceAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminResolveBinding;
import com.ujjwalkumar.eplacements.models.Grievance;
import com.ujjwalkumar.eplacements.utilities.EPlacementsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminResolveActivity extends AppCompatActivity {

    private String statusFilter;
    private ArrayList<Grievance> al;
    private ActivityAdminResolveBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminResolveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EPlacementsUtil.checkInternetConnection(this);

        statusFilter = "";
        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.imageViewFilter.setOnClickListener(view -> {
            statusFilter = "";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Filter")
                    .setCancelable(false)
                    .setSingleChoiceItems(new String[]{"All", "Resolved", "Unresolved"}, -1, (dialog, which) -> {
                        if(which==0)
                            statusFilter = "";
                        else if(which==1)
                            statusFilter = "resolved";
                        else
                            statusFilter = "unresolved";
                    })
                    .setPositiveButton("Yes", (dialog, id) -> {
                        showInformation();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .show();
        });
    }

    private void showInformation() {
        binding.textViewStatus.setText(statusFilter);
        startLoading();
        String url = getString(R.string.base_url) + "admin/getGrievance";
        JSONObject postData = new JSONObject();
        try {
            postData.put("status", statusFilter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray grievances = response.getJSONArray("grievances");
                            al = new ArrayList<>();
                            for(int i=0; i<grievances.length(); i++) {
                                JSONObject obj = grievances.getJSONObject(i);
                                Grievance grievance = new Grievance(obj.getString("_id"), obj.getString("reg_no"), obj.getString("name"), obj.getString("email"), obj.getString("message"), obj.getString("status"), obj.getLong("timestamp"));
                                al.add(grievance);
                            }
                            GrievanceAdapter adapter = new GrievanceAdapter(AdminResolveActivity.this, user.getString("token", ""), al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminResolveActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(AdminResolveActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminResolveActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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