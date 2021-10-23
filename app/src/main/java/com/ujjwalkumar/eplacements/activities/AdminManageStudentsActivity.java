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
import com.ujjwalkumar.eplacements.adapters.StudentAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminManageStudentsBinding;
import com.ujjwalkumar.eplacements.models.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminManageStudentsActivity extends AppCompatActivity {

    private String statusFilter;
    private ArrayList<Student> al;
    private ActivityAdminManageStudentsBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminManageStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    .setSingleChoiceItems(new String[]{"All", "Registered", "Verified", "Unverified", "Placed"}, -1, (dialog, which) -> {
                        if(which==0)
                            statusFilter = "";
                        else if(which==1)
                            statusFilter = "registered";
                        else if(which==2)
                            statusFilter = "verified";
                        else if(which==3)
                            statusFilter = "unverified";
                        else
                            statusFilter = "placed";
                    })
                    .setPositiveButton("Yes", (dialog, id) -> {
                        showInformation();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .show();
        });
    }

    private void showInformation() {
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url) + "admin/getStudents";
        JSONObject postData = new JSONObject();
        try {
            postData.put("get_status", statusFilter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray students = response.getJSONArray("students");
                            al = new ArrayList<>();
                            for(int i=0; i<students.length(); i++) {
                                JSONObject obj = students.getJSONObject(i);
                                String degreeCourse = obj.getString("course") + " - " + obj.get("branch");
                                Student student = new Student(obj.getString("name"), obj.getString("reg_no"), degreeCourse, obj.getString("status"));
                                al.add(student);
                            }
                            StudentAdapter adapter = new StudentAdapter(AdminManageStudentsActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminManageStudentsActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(AdminManageStudentsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminManageStudentsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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