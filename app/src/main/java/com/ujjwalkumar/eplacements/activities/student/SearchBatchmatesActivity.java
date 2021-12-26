package com.ujjwalkumar.eplacements.activities.student;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivitySearchBatchmatesBinding;
import com.ujjwalkumar.eplacements.utilities.Triplet;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchBatchmatesActivity extends AppCompatActivity {

    private ActivitySearchBatchmatesBinding binding;
    private SharedPreferences user;
    private ArrayList<Triplet> students = new ArrayList<>();
    private ArrayList<String> studentNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBatchmatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                startLoading();
            }
        });

        binding.autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            String key = (String) adapterView.getItemAtPosition(i);
            int pos = studentNames.indexOf(key);
            binding.textViewName.setText(students.get(pos).first);
            binding.textViewRegNo.setText(students.get(pos).second);
            binding.textViewCompany.setText(students.get(pos).third);
            binding.autoCompleteTextView.setText("");
            stopLoading();
        });

        loadStudents();
    }

    private void loadStudents() {
        startLoading();
        String url = getString(R.string.base_url) + "student/searchStudent";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray stud = response.getJSONArray("students");
                            students = new ArrayList<>();
                            studentNames = new ArrayList<>();
                            for(int i=0; i<stud.length(); i++) {
                                String name = stud.getJSONObject(i).getString("name");
                                String reg_no = stud.getJSONObject(i).getString("reg_no");
                                String company_name = stud.getJSONObject(i).getString("company_name");
                                students.add(new Triplet(name, reg_no, company_name));
                                studentNames.add(reg_no + " - " + name);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, studentNames);
                            binding.autoCompleteTextView.setThreshold(2);                       // will start working from first character
                            binding.autoCompleteTextView.setAdapter(adapter);                   // setting the adapter data into the AutoCompleteTextView
                            Toast.makeText(SearchBatchmatesActivity.this, "Search students", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SearchBatchmatesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(SearchBatchmatesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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
        binding.detailsView.setVisibility(View.INVISIBLE);
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
    }

    private void stopLoading() {
        binding.detailsView.setVisibility(View.VISIBLE);
        binding.animationViewLoading.setVisibility(View.GONE);
    }
}