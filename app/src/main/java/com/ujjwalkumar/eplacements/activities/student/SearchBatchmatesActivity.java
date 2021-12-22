package com.ujjwalkumar.eplacements.activities.student;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivitySearchBatchmatesBinding;
import com.ujjwalkumar.eplacements.utilities.Triplet;

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
            stopLoading();
        });

        loadStudents();
    }

    private void loadStudents() {
        startLoading();
        String url = getString(R.string.base_url) + "student/searchBatchmates";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {

                        }
                        else
                            Toast.makeText(SearchBatchmatesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
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
    }

    private void stopLoading() {
        binding.animationViewLoading.setVisibility(View.GONE);
    }
}