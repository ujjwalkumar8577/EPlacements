package com.ujjwalkumar.eplacements.activities.admin;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.common.NoticesActivity;
import com.ujjwalkumar.eplacements.activities.common.StatisticsActivity;
import com.ujjwalkumar.eplacements.activities.common.UpcomingCompaniesActivity;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminHomeBinding;

import java.util.HashMap;
import java.util.Map;

public class AdminHomeActivity extends AppCompatActivity {

    private ActivityAdminHomeBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        FirebaseMessaging.getInstance().subscribeToTopic("allAdmins");

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();

        binding.imageViewAccount.setOnClickListener(view -> startActivity(new Intent(this, AdminMyAccountActivity.class)));

        binding.imageViewNotice.setOnClickListener(view -> startActivity(new Intent(this, NoticesActivity.class)));

        binding.layoutMenu1.setOnClickListener(view -> startActivity(new Intent(this, AdminAddCompanyActivity.class)));

        binding.layoutMenu2.setOnClickListener(view -> startActivity(new Intent(this, UpcomingCompaniesActivity.class)));

        binding.layoutMenu3.setOnClickListener(view -> startActivity(new Intent(this, AdminManageStudentsActivity.class)));

        binding.layoutMenu4.setOnClickListener(view -> startActivity(new Intent(this, StatisticsActivity.class)));

        binding.layoutMenu5.setOnClickListener(view -> startActivity(new Intent(this, AdminAnnounceResultActivity.class)));

        binding.layoutMenu6.setOnClickListener(view -> startActivity(new Intent(this, AdminResolveActivity.class)));
    }

    private void showInformation() {
        startLoading();
        binding.textViewName.setText(user.getString("name", ""));
        binding.textViewRole.setText(user.getString("course", ""));

        String url = getString(R.string.base_url) + "admin/getInsights";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            int totalCompanies = response.getInt("companiesVisited");
                            int totalRegistrations = response.getInt("registrations");
                            int verifiedStudents = response.getInt("verfiedStudents");
                            int placedStudents = response.getInt("placedStudents");
                            double percentage = 0;
                            if(verifiedStudents!=0)
                                percentage = 100.0*placedStudents/verifiedStudents;

                            binding.textViewTotalCompanies.setText(String.valueOf(totalCompanies));
                            binding.textViewTotalRegistrations.setText(String.valueOf(totalRegistrations));
                            binding.textViewVerifiedStudents.setText(String.valueOf(verifiedStudents));
                            binding.textViewPlacedStudents.setText(String.valueOf(placedStudents));
                            binding.textViewPercentage.setText(String.format("%.2f",percentage));
                        }
                        else
                            Toast.makeText(AdminHomeActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminHomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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
        binding.animationViewLoading.playAnimation();
        binding.animationViewLoading.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        binding.animationViewLoading.pauseAnimation();
        binding.animationViewLoading.setVisibility(View.GONE);
    }
}