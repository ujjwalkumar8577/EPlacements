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
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.common.ContactsActivity;
import com.ujjwalkumar.eplacements.activities.common.NoticesActivity;
import com.ujjwalkumar.eplacements.activities.common.StatisticsActivity;
import com.ujjwalkumar.eplacements.activities.common.UpcomingCompaniesActivity;
import com.ujjwalkumar.eplacements.databinding.ActivityHomeBinding;
import com.ujjwalkumar.eplacements.models.Experience;

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
        updateInformation();

        binding.imageViewAccount.setOnClickListener(view -> startActivity(new Intent(this, MyAccountActivity.class)));

        binding.imageViewNotice.setOnClickListener(view -> startActivity(new Intent(this, NoticesActivity.class)));

        binding.layoutMenu1.setOnClickListener(view -> startActivity(new Intent(this, UpcomingCompaniesActivity.class)));

        binding.layoutMenu2.setOnClickListener(view -> startActivity(new Intent(this, RegisteredCompaniesActivity.class)));

        binding.layoutMenu3.setOnClickListener(view -> startActivity(new Intent(this, ViewExperienceActivity.class)));

        binding.layoutMenu4.setOnClickListener(view -> checkEligibility());

        binding.layoutMenu5.setOnClickListener(view -> startActivity(new Intent(this, ContactsActivity.class)));

        binding.layoutMenu6.setOnClickListener(view -> startActivity(new Intent(this, HelpActivity.class)));

        binding.layoutMenu7.setOnClickListener(view -> startActivity(new Intent(this, StatisticsActivity.class)));

        binding.layoutMenu8.setOnClickListener(view -> startActivity(new Intent(this, SearchBatchmatesActivity.class)));
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

    private void updateInformation() {
        String url = getString(R.string.base_url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            String name = response.getJSONObject("user").getString("name");
                            String regno = response.getJSONObject("user").getString("reg_no");
                            String course = response.getJSONObject("user").getString("course");
                            String branch = response.getJSONObject("user").getString("branch");
                            String status = response.getJSONObject("user").getString("status");
                            String credits = response.getJSONObject("user").getString("credits");
                            user.edit().putString("name", name);
                            user.edit().putString("reg_no", regno);
                            user.edit().putString("course", course);
                            user.edit().putString("branch", branch);
                            user.edit().putString("status", status);
                            user.edit().putString("credits", credits);
                            showInformation();
                        }
                        else
                            Toast.makeText(HomeActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
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

    private void checkEligibility() {
        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
        View promptsView = li.inflate(R.layout.dialog_check_eligibility, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        String url = getString(R.string.base_url) + "student/checkEligibility";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success") && response.getBoolean("isEligible")) {
                            String company_name = response.getString("company_name");
                            String company_profile = response.getString("company_profile");
                            String company_location = response.getString("company_location");

                            Experience experience = new Experience("2021", user.getString("id", ""), user.getString("name", ""), company_name, company_profile, company_location, "" ,5, 1640079614360L);
                            startActivity(new Intent(this, AddExperienceActivity.class)
                                    .putExtra("action", "add")
                                    .putExtra("data", new Gson().toJson(experience)));
                        }
                        else if(response.getBoolean("success") && !response.getBoolean("isEligible")) {
                            JSONObject experience = response.getJSONObject("experience");
                            startActivity(new Intent(this, AddExperienceActivity.class)
                                    .putExtra("action", "edit")
                                    .putExtra("data", experience.toString()));
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