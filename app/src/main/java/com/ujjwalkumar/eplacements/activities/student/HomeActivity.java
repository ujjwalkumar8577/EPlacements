package com.ujjwalkumar.eplacements.activities.student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.common.ContactsActivity;
import com.ujjwalkumar.eplacements.activities.common.NoticesActivity;
import com.ujjwalkumar.eplacements.activities.common.StatisticsActivity;
import com.ujjwalkumar.eplacements.activities.common.UpcomingCompaniesActivity;
import com.ujjwalkumar.eplacements.databinding.ActivityHomeBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private SharedPreferences user;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("allDevices");
        FirebaseMessaging.getInstance().subscribeToTopic("allStudents");

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        showInformation();
//        EPlacementsUtil.showToast(this, "An error occured", R.drawable.outline_error_white_48dp);

        binding.imageViewAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, MyAccountActivity.class));
        });

        binding.imageViewNotice.setOnClickListener(view -> {
            startActivity(new Intent(this, NoticesActivity.class));
        });

        binding.layoutMenu1.setOnClickListener(view -> {
            startActivity(new Intent(this, UpcomingCompaniesActivity.class));
        });

        binding.layoutMenu2.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisteredCompaniesActivity.class));
        });

        binding.layoutMenu3.setOnClickListener(view -> {
            startActivity(new Intent(this, ViewExperienceActivity.class));
        });

        binding.layoutMenu4.setOnClickListener(view -> {
            checkEligibility();
        });

        binding.layoutMenu5.setOnClickListener(view -> {
            startActivity(new Intent(this, ContactsActivity.class));
        });

        binding.layoutMenu6.setOnClickListener(view -> {
            startActivity(new Intent(this, HelpActivity.class));
        });

        binding.layoutMenu7.setOnClickListener(view -> {
            startActivity(new Intent(this, StatisticsActivity.class));
        });

        binding.layoutMenu8.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchBatchmatesActivity.class));
        });
    }

    private void showInformation() {
        binding.textViewName.setText(user.getString("name", ""));
        binding.textViewDegreeCourse.setText(user.getString("course", "") + " - " + user.getString("branch", ""));
        binding.textViewCredits.setText(user.getString("credits", ""));
        binding.textViewStatus.setText(getStatus(user.getString("status", "")));
        if(user.getString("status", "").equals("registered") || user.getString("status", "").equals("unverified"))
            binding.imageViewVerified.setVisibility(View.GONE);
        else
            binding.imageViewVerified.setVisibility(View.VISIBLE);
    }

    private void checkEligibility() {
        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
        View promptsView = li.inflate(R.layout.dialog_check_eligibility, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        String url = getString(R.string.base_url) + "/student/checkEligibility";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success") && response.getBoolean("isEligible")) {
                            JSONObject company = response.getJSONObject("company");
                            String company_id = company.getString("company_id");
                            String company_name = company.getString("name");
                            String company_profile = company.getString("job_profile");
                            String company_location = company.getString("job_location");
                            startActivity(new Intent(this, AddExperienceActivity.class)
                                    .putExtra("action", "add")
                                    .putExtra("name", user.getString("name", ""))
                                    .putExtra("company_name", company_name)
                                    .putExtra("company_profile", company_profile)
                                    .putExtra("company_location", company_location));
                        }
                        else if(response.getBoolean("success") && !response.getBoolean("isEligible")) {
                            JSONObject company = response.getJSONObject("company");
                            String company_id = company.getString("company_id");
                            String company_name = company.getString("name");
                            String company_profile = company.getString("job_profile");
                            String company_location = company.getString("job_location");
                            startActivity(new Intent(this, AddExperienceActivity.class)
                                    .putExtra("action", "edit")
                                    .putExtra("data", user.getString("name", "")));
                        }
                        else
                            Toast.makeText(HomeActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } catch (Exception e) {
                        alertDialog.dismiss();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private String getStatus(String status) {
        if(status.equals("placed"))
            return "Offered";
        else
            return "Not Offered";
    }
}