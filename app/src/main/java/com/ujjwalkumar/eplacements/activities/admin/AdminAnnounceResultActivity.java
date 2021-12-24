package com.ujjwalkumar.eplacements.activities.admin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.adapters.SelectedStudentAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminAnnounceResultBinding;
import com.ujjwalkumar.eplacements.utilities.Triplet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminAnnounceResultActivity extends AppCompatActivity {

    private ActivityAdminAnnounceResultBinding binding;
    private SharedPreferences user;
    private ArrayList<Pair<String, String>> companies = new ArrayList<>();
    private String company_id = "";
    private ArrayList<Triplet> allStudents = new ArrayList<>();
    private ArrayList<Triplet> selectedStudents = new ArrayList<>();
    private ArrayList<String> companyNames = new ArrayList<>();
    private ArrayList<String> studentNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAnnounceResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                company_id = companies.get(binding.spinnerCompany.getSelectedItemPosition()).second;
                loadStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String key = (String) adapterView.getItemAtPosition(i);
            int pos = studentNames.indexOf(key);
            if(!selectedStudents.contains(allStudents.get(pos))) {
                selectedStudents.add(allStudents.get(pos));
                SelectedStudentAdapter adapter = new SelectedStudentAdapter(AdminAnnounceResultActivity.this, selectedStudents);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminAnnounceResultActivity.this));
                binding.recyclerView.setAdapter(adapter);
            }
            else {
                Toast.makeText(AdminAnnounceResultActivity.this, "Already selected", Toast.LENGTH_SHORT).show();
            }
            binding.autoCompleteTextView.setText("");
        });
        
        binding.buttonDone.setOnClickListener(view -> {
            if(selectedStudents.size()>0)
                addResult();
            else
                Toast.makeText(AdminAnnounceResultActivity.this, "No Student selected", Toast.LENGTH_SHORT).show();
        });

        loadCompanies();
    }

    private void loadCompanies() {
        startLoading();
        String url = getString(R.string.base_url) + "admin/loadCompanies";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray comp = response.getJSONArray("companies");
                            companies = new ArrayList<>();
                            companyNames = new ArrayList<>();
                            for(int i=0; i<comp.length(); i++) {
                                String id = comp.getJSONObject(i).getString("_id");
                                String name = comp.getJSONObject(i).getString("name");
                                companies.add(new Pair<>(name, id));
                                companyNames.add(name);
                            }
                            binding.spinnerCompany.setAdapter(new ArrayAdapter<>(AdminAnnounceResultActivity.this, android.R.layout.simple_list_item_1, companyNames));
                        }
                        else
                            Toast.makeText(AdminAnnounceResultActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminAnnounceResultActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void loadStudents() {
        startLoading();
        String url = getString(R.string.base_url) + "admin/loadStudents";
        JSONObject postData = new JSONObject();
        try {
            postData.put("company_id", company_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray stud = response.getJSONArray("students");
                            allStudents = new ArrayList<>();
                            selectedStudents = new ArrayList<>();
                            studentNames = new ArrayList<>();
                            for(int i=0; i<stud.length(); i++) {
                                String name = stud.getJSONObject(i).getString("name");
                                String reg_no = stud.getJSONObject(i).getString("reg_no");
                                String reg_id = stud.getJSONObject(i).getString("_id");
                                allStudents.add(new Triplet(name, reg_no, reg_id));
                                studentNames.add(reg_no + " - " + name);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, studentNames);
                            binding.autoCompleteTextView.setThreshold(2);                       // will start working from first character
                            binding.autoCompleteTextView.setAdapter(adapter);                   // setting the adapter data into the AutoCompleteTextView
                        }
                        else
                            Toast.makeText(AdminAnnounceResultActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminAnnounceResultActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    
    private void addResult() {
        startLoading();
        String url = getString(R.string.base_url) + "admin/addResult";
        JSONObject postData = new JSONObject();
        try {
            JSONArray reg_nos = new JSONArray();
            for(Triplet triplet: selectedStudents)
                reg_nos.put(triplet.third);
            postData.put("company_id", company_id);
            postData.put("reg_nos", reg_nos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            Toast.makeText(AdminAnnounceResultActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(AdminAnnounceResultActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminAnnounceResultActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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
        binding.animationViewLoading.setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        binding.animationViewLoading.setVisibility(View.GONE);
    }
}