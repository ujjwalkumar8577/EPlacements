package com.ujjwalkumar.eplacements.activities.admin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
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

//        companies.add("Google", "848");
//        companies.add("Microsoft", "848");
//        companies.add("Facebook", "848");
//        companies.add("Amazon", "848");
//        allStudents.add(new Pair<>("Aman Gupta", "20197698"));
//        allStudents.add(new Pair<>("Anuj Mishra", "20191698"));
//        allStudents.add(new Pair<>("Vishal Verma", "20197498"));
//        allStudents.add(new Pair<>("Utkarsh Singh", "20191098"));

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        SelectedStudentAdapter adapter = new SelectedStudentAdapter(AdminAnnounceResultActivity.this, selectedStudents);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminAnnounceResultActivity.this));
        binding.recyclerView.setAdapter(adapter);

        binding.spinnerCompany.setOnItemClickListener((adapterView, view, i, l) -> {
            company_id = companies.get(binding.spinnerCompany.getSelectedItemPosition()).second;
            loadStudents();
        });

        binding.autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String key = (String) adapterView.getItemAtPosition(i);
            int pos = studentNames.indexOf(key);
            if(!selectedStudents.contains(allStudents.get(pos))) {
                selectedStudents.add(allStudents.get(pos));
                binding.recyclerView.getAdapter().notifyDataSetChanged();
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

    private void initializeFields() {
        binding.animationViewLoading.pauseAnimation();
        binding.animationViewLoading.setVisibility(View.GONE);

        binding.spinnerCompany.setAdapter(new ArrayAdapter<String>(AdminAnnounceResultActivity.this, android.R.layout.simple_list_item_1, companyNames));

        studentNames = new ArrayList<>();
        for(Triplet triplet: allStudents) {
            studentNames.add(triplet.second + " - " + triplet.first);
        }
        //Creating the instance of ArrayAdapter containing list of customers
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, studentNames);

        //Getting the instance of AutoCompleteTextView
        binding.autoCompleteTextView.setThreshold(2);                       //will start working from first character
        binding.autoCompleteTextView.setAdapter(adapter);                   //setting the adapter data into the AutoCompleteTextView
    }

    private void loadCompanies() {
        startLoading();
        String url = getString(R.string.base_url) + "admin/loadCompanies";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {

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
        String url = getString(R.string.base_url) + "admin/loadStudents";
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