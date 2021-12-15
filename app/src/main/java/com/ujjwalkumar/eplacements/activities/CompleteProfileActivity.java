package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityCompleteProfileBinding;
import com.ujjwalkumar.eplacements.models.StudentProfile;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompleteProfileActivity extends AppCompatActivity {

    private boolean dateSet = false;
    private int select = -1;
    private ActivityCompleteProfileBinding binding;
    private SharedPreferences user;
    private StudentProfile studentProfile;
    private Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String str = getIntent().getStringExtra("userObj");
        studentProfile = gson.fromJson(str, StudentProfile.class);

        if(studentProfile.status.equals("verified") || studentProfile.status.equals("placed"))
            binding.buttonUpdateProfile.setVisibility(View.GONE);

        if(user.getString("type", "").equals("admin"))
            binding.buttonUpdateProfile.setVisibility(View.GONE);
        else
            binding.buttonUpdateStatus.setVisibility(View.GONE);

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.textViewDOB.setOnClickListener(view -> {
            showDateTimePicker();
        });

        binding.checkBoxSame.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b)
                binding.editTextPermanentAddress.setText(binding.editTextPresentAddress.getText().toString());
        });

        binding.buttonUpdateProfile.setOnClickListener(view -> {
            updateObject();
            updateStudent();
        });

        binding.buttonUpdateStatus.setOnClickListener(view -> {
            String[] arr = getResources().getStringArray(R.array.status);
            select = search(arr, studentProfile.status);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update status")
                    .setCancelable(false)
                    .setSingleChoiceItems(arr, select, (dialog, which) -> select = which)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        if(select>0)
                            updateStatus(arr[select], "");
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .show();
        });

        if(studentProfile.remarks!=null)
            initialiseValues();
        else
            getUser();
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        cal = Calendar.getInstance();

        new DatePickerDialog(CompleteProfileActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(year, monthOfYear, dayOfMonth);
            binding.textViewDOB.setText(new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime()));
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void initialiseValues() {
        dateSet = !studentProfile.dob.equals("");
        binding.editTextName.setText(studentProfile.name);
        binding.spinnerCourse.setSelection(search(getResources().getStringArray(R.array.course), studentProfile.course));
        binding.spinnerBranch.setSelection(search(getResources().getStringArray(R.array.branch), studentProfile.branch));
        binding.textViewDOB.setText(studentProfile.dob);
        binding.editTextEmail.setText(studentProfile.email);
        binding.editTextSkypeID.setText(studentProfile.skype_id);
        binding.editTextLinkedinID.setText(studentProfile.linkedin_id);
        binding.spinnerGender.setSelection(search(getResources().getStringArray(R.array.gender), studentProfile.gender));
        binding.spinnerCategory.setSelection(search(getResources().getStringArray(R.array.category), studentProfile.category));
        if(studentProfile.physically_challenged)
            binding.checkBoxYes.setChecked(true);
        else
            binding.checkBoxNo.setChecked(true);
        if(studentProfile.residential_status.equals("Hosteller"))
            binding.checkBoxHosteller.setChecked(true);
        else
            binding.checkBoxDayScholar.setChecked(true);
        binding.editTextGuardianName.setText(studentProfile.guardian);
        binding.editTextPresentAddress.setText(studentProfile.present_address);
        binding.editTextPermanentAddress.setText(studentProfile.permanent_address);
        if(studentProfile.maritial_status.equals("Married"))
            binding.checkBoxMarried.setChecked(true);
        else
            binding.checkBoxSingle.setChecked(true);
        binding.editTextCountry.setText(studentProfile.country);
        binding.editTextState.setText(studentProfile.state);
        binding.editTextSchool10.setText(studentProfile.school_10);
        binding.editTextBoard10.setText(studentProfile.board_10);
        binding.editTextYear10.setText(String.valueOf(studentProfile.year_10));
        binding.editTextPercent10.setText(String.valueOf(studentProfile.percent_10));
        binding.editTextSchool12.setText(studentProfile.school_12);
        binding.editTextBoard12.setText(studentProfile.board_12);
        binding.editTextYear12.setText(String.valueOf(studentProfile.year_12));
        binding.editTextPercent12.setText(String.valueOf(studentProfile.percent_12));
        binding.editTextBacklogs.setText(String.valueOf(studentProfile.backlogs));
        binding.editTextCpi.setText(String.valueOf(studentProfile.cpi));
        binding.editTextProjectTitle.setText(studentProfile.project_title);
        binding.editTextProjectDesc.setText(studentProfile.project_desc);
        binding.editTextInternTitle.setText(studentProfile.intern_title);
        binding.editTextInternDesc.setText(studentProfile.intern_desc);
    }

    private void updateObject() {
        studentProfile.name = binding.editTextName.getText().toString();
        studentProfile.course = binding.spinnerCourse.getSelectedItem().toString();
        studentProfile.branch = binding.spinnerBranch.getSelectedItem().toString();
        if(!dateSet)
            studentProfile.dob = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
        studentProfile.email = binding.editTextEmail.getText().toString();
        studentProfile.skype_id = binding.editTextSkypeID.getText().toString();
        studentProfile.linkedin_id = binding.editTextLinkedinID.getText().toString();
        studentProfile.gender = binding.spinnerGender.getSelectedItem().toString();
        studentProfile.category = binding.spinnerCategory.getSelectedItem().toString();
        studentProfile.physically_challenged = binding.checkBoxYes.isChecked();
        if(binding.checkBoxHosteller.isChecked())
            studentProfile.residential_status = "Hosteller";
        else
            studentProfile.residential_status = "Day Scholar";
        studentProfile.guardian = binding.editTextGuardianName.getText().toString();
        studentProfile.present_address = binding.editTextPresentAddress.getText().toString();
        studentProfile.permanent_address = binding.editTextPermanentAddress.getText().toString();
        if(binding.checkBoxMarried.isChecked())
            studentProfile.maritial_status = "Married";
        else
            studentProfile.maritial_status = "Single";
        studentProfile.country = binding.editTextCountry.getText().toString();
        studentProfile.state = binding.editTextState.getText().toString();
        studentProfile.school_10 = binding.editTextSchool10.getText().toString();
        studentProfile.board_10 = binding.editTextBoard10.getText().toString();
        studentProfile.year_10 = Integer.parseInt(binding.editTextYear10.getText().toString());
        studentProfile.percent_10 = Float.parseFloat(binding.editTextPercent10.getText().toString());
        studentProfile.school_12 = binding.editTextSchool12.getText().toString();
        studentProfile.board_12 = binding.editTextBoard12.getText().toString();
        studentProfile.year_12 = Integer.parseInt(binding.editTextYear12.getText().toString());
        studentProfile.percent_12 = Float.parseFloat(binding.editTextPercent12.getText().toString());
        studentProfile.backlogs = Integer.parseInt(binding.editTextBacklogs.getText().toString());
        studentProfile.cpi = Float.parseFloat(binding.editTextCpi.getText().toString());
        studentProfile.project_title = binding.editTextProjectTitle.getText().toString();
        studentProfile.project_desc = binding.editTextProjectDesc.getText().toString();
        studentProfile.intern_title = binding.editTextInternTitle.getText().toString();
        studentProfile.intern_desc = binding.editTextInternDesc.getText().toString();
    }

    public void updateStudent() {
        String url = getString(R.string.base_url) + "student/updateStudent";
        JSONObject postData = new JSONObject();
        try {
            Field[] fields = StudentProfile.class.getDeclaredFields();
            for(Field field: fields) {
                postData.put(field.getName(), field.get(studentProfile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {

                        }
                        Toast.makeText(CompleteProfileActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(CompleteProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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

    public void updateStatus(String updatedStatus, String remarks) {
        String url = getString(R.string.base_url) + "admin/setStatus";
        JSONObject postData = new JSONObject();
        try {
            studentProfile.status = updatedStatus;
            studentProfile.remarks = remarks;
            postData.put("reg_no", studentProfile.reg_no);
            postData.put("set_status", studentProfile.status);
            postData.put("remark", studentProfile.remarks);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {

                        }
                        Toast.makeText(CompleteProfileActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(CompleteProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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

    public void reduceCredit(int credit)  {
        String url = getString(R.string.base_url) + "admin/reduceCredit";
        JSONObject postData = new JSONObject();
        try {
            postData.put("reg_no", studentProfile.reg_no);
            postData.put("credit", credit);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            studentProfile.credits = response.getInt("final_credits");
                        }
                        Toast.makeText(CompleteProfileActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(CompleteProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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

    private void getUser() {

    }

    private int search(String[] arr, String key) {
        for(int i=0; i<arr.length; i++) {
            if(arr[i].equals(key))
                return i;
        }
        return 0;
    }
}