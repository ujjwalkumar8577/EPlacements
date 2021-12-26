package com.ujjwalkumar.eplacements.activities.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminAddCompanyBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AdminAddCompanyActivity extends AppCompatActivity {

    String name, profile, location, description, ppo, process, ctc, minCpi, min10, min12, gaps, eligibility, link, remarks;
    Boolean dateSet = false;
    private HashSet<String> set;
    private ActivityAdminAddCompanyBinding binding;
    private SharedPreferences user;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        set = new HashSet<>();

        binding.imageViewBack.setOnClickListener(view -> super.onBackPressed());

        binding.buttonAdd.setOnClickListener(view -> {
            name = binding.editTextName.getText().toString();
            profile = binding.editTextProfile.getText().toString();
            location = binding.editTextLocation.getText().toString();
            description = binding.editTextDescription.getText().toString();
            ppo = binding.editTextPpo.getText().toString();
            process = binding.editTextProcess.getText().toString();
            ctc = binding.editTextCtc.getText().toString();
            minCpi = binding.editTextMinCpi.getText().toString();
            min10 = binding.editTextMin10.getText().toString();
            min12 = binding.editTextMin12.getText().toString();
            gaps = binding.editTextGaps.getText().toString();
            eligibility = binding.editTextEligibility.getText().toString();
            link = binding.editTextLink.getText().toString();
            remarks = binding.editTextRemarks.getText().toString();

            if(!name.equals("")) {
                if(!profile.equals("")) {
                    if(!location.equals("")) {
                        if(!description.equals("")) {
                            if(!ppo.equals("")) {
                                if(!process.equals("")) {
                                    if(!ctc.equals("")) {
                                        if(!set.isEmpty()) {
                                            if(dateSet) {
                                                if(!minCpi.equals("")) {
                                                    if(!min10.equals("")) {
                                                        if(!min12.equals("")) {
                                                            if(!gaps.equals("")) {
                                                                addCompany();
                                                            } else binding.editTextGaps.setError("Required Field");
                                                        } else binding.editTextMin12.setError("Required Field");
                                                    } else binding.editTextMin10.setError("Required Field");
                                                } else binding.editTextMinCpi.setError("Required Field");
                                            } else Toast.makeText(AdminAddCompanyActivity.this, "Select registration deadline", Toast.LENGTH_SHORT).show();
                                        } else Toast.makeText(AdminAddCompanyActivity.this, "Select allowed branches", Toast.LENGTH_SHORT).show();
                                    } else binding.editTextCtc.setError("Required field");
                                } else binding.editTextProcess.setError("Required field");
                            } else binding.editTextPpo.setError("Required field");
                        } else binding.editTextDescription.setError("Required field");
                    } else binding.editTextLocation.setError("Required field");
                } else binding.editTextProfile.setError("Required field");
            } else binding.editTextName.setError("Required field");
        });

        binding.checkBox1.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox1.getText().toString());
            else
                set.remove(binding.checkBox1.getText().toString());
        });

        binding.checkBox2.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox2.getText().toString());
            else
                set.remove(binding.checkBox2.getText().toString());
        });

        binding.checkBox3.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox3.getText().toString());
            else
                set.remove(binding.checkBox3.getText().toString());
        });

        binding.checkBox4.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox4.getText().toString());
            else
                set.remove(binding.checkBox4.getText().toString());
        });

        binding.checkBox5.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox5.getText().toString());
            else
                set.remove(binding.checkBox5.getText().toString());
        });

        binding.checkBox6.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox6.getText().toString());
            else
                set.remove(binding.checkBox6.getText().toString());
        });

        binding.checkBox7.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox7.getText().toString());
            else
                set.remove(binding.checkBox7.getText().toString());
        });

        binding.checkBox8.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox8.getText().toString());
            else
                set.remove(binding.checkBox8.getText().toString());
        });

        binding.checkBox9.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                set.add(binding.checkBox9.getText().toString());
            else
                set.remove(binding.checkBox9.getText().toString());
        });

        binding.textViewDeadline.setOnClickListener(view -> showDateTimePicker());
    }

    private void initializeFields() {
        binding.editTextName.setText("");
        binding.editTextProfile.setText("");
        binding.editTextLocation.setText("");
        binding.editTextDescription.setText("");
        binding.editTextPpo.setText("");
        binding.editTextProcess.setText("");
        binding.editTextCtc.setText("");
        binding.editTextMinCpi.setText("");
        binding.editTextMin10.setText("");
        binding.editTextMin12.setText("");
        binding.editTextGaps.setText("");
        binding.editTextEligibility.setText("");
        binding.editTextLink.setText("");
        binding.editTextRemarks.setText("");
        binding.checkBox1.setChecked(false);
        binding.checkBox2.setChecked(false);
        binding.checkBox3.setChecked(false);
        binding.checkBox4.setChecked(false);
        binding.checkBox5.setChecked(false);
        binding.checkBox6.setChecked(false);
        binding.checkBox7.setChecked(false);
        binding.checkBox8.setChecked(false);
        binding.checkBox9.setChecked(false);
        set.clear();
        dateSet = false;
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        cal = Calendar.getInstance();

        new DatePickerDialog(AdminAddCompanyActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(AdminAddCompanyActivity.this, (view1, hourOfDay, minute) -> {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                binding.textViewDeadline.setText(cal.getTime().toString());
                dateSet = true;
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void addCompany() {
        String url = getString(R.string.base_url) + "admin/addCompany";
        JSONObject postData = new JSONObject();
        try {
            postData.put("name", name);
            postData.put("job_profile", profile);
            postData.put("job_location", location);
            postData.put("job_desc", description);
            postData.put("provision_ppo", ppo);
            postData.put("process", process);
            postData.put("ctc", ctc);
            postData.put("allowed_branches", new JSONArray(set));
            postData.put("reg_deadline", cal.getTimeInMillis());
            postData.put("company_link", link);
            postData.put("eligibility_criteria", eligibility);
            postData.put("min_cpi", Double.parseDouble(minCpi));
            postData.put("min_10", Double.parseDouble(min10));
            postData.put("min_12", Double.parseDouble(min12));
            postData.put("gap_allowed", Integer.parseInt(gaps));
            postData.put("remarks", remarks);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("post data", postData.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            initializeFields();
                        }
                        Toast.makeText(AdminAddCompanyActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(AdminAddCompanyActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/json");
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}