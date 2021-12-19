package com.ujjwalkumar.eplacements.activities.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
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
import com.ujjwalkumar.eplacements.adapters.KeyValueAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityCompanyDetailsBinding;
import com.ujjwalkumar.eplacements.utilities.EPlacementsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompanyDetailsActivity extends AppCompatActivity {

    private String name, id;
    private boolean isRegistered, agreed;
    private ArrayList<Pair<String, String>> al;
    private ActivityCompanyDetailsBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EPlacementsUtil.checkInternetConnection(this);

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        isRegistered = getIntent().getBooleanExtra("registered", false);
        agreed = false;
        if(isRegistered || user.getString("type", "").equals("admin"))
            binding.buttonRegister.setVisibility(View.GONE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.buttonRegister.setOnClickListener(view -> {
            agreed = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Agree Rules")
                    .setCancelable(false)
                    .setSingleChoiceItems(new String[]{"I Agree that once registered i will participate in every further round of company"}, -1, (dialog, which) -> agreed = true)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        if(agreed)
                            registerForCompany();
                        else
                            Toast.makeText(CompanyDetailsActivity.this, "Agree to rules first", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                    .show();
        });
    }

    private void showInformation() {
        binding.textView3.setText(name);
        startLoading();
        String url = getString(R.string.base_url) + "getCompany";
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONObject company = response.getJSONObject("company");
                            al = new ArrayList<>();
                            al.add(new Pair<>("Name", company.getString("name")));
                            al.add(new Pair<>("Job Profile", company.getString("job_profile")));
                            al.add(new Pair<>("Job Location", company.getString("job_location")));
                            al.add(new Pair<>("Job Description", company.getString("job_desc")));
                            al.add(new Pair<>("Provision for PPO", company.getString("provision_ppo")));
                            al.add(new Pair<>("Process", company.getString("process")));
                            al.add(new Pair<>("CTC", company.getString("ctc") + " LPA"));
                            al.add(new Pair<>("Allowed Branches", getAllowedBranches(company.getJSONArray("allowed_branches"))));
                            al.add(new Pair<>("Registration Deadline", getTimeString(company.getLong("reg_deadline"))));
                            al.add(new Pair<>("Company Link", company.getString("company_link")));
                            al.add(new Pair<>("Minimum CPI", company.getString("min_cpi")));
                            al.add(new Pair<>("Minimum 10th %", company.getString("min_10")));
                            al.add(new Pair<>("Minimum 12th %", company.getString("min_12")));
                            al.add(new Pair<>("Gaps Allowed", company.getString("gap_allowed") + " years"));
                            al.add(new Pair<>("Other Eligibility Criteria", company.getString("eligibility_criteria")));
                            al.add(new Pair<>("Remarks", company.getString("remarks")));

                            KeyValueAdapter adapter = new KeyValueAdapter(this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(CompanyDetailsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(CompanyDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void registerForCompany() {
        startLoading();
        String url = getString(R.string.base_url) + "student/registerForCompany";
        JSONObject postData = new JSONObject();
        try {
            postData.put("reg_no", user.getString("id", ""));
            postData.put("name", user.getString("name", ""));
            postData.put("company_id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        stopLoading();
                        Toast.makeText(CompanyDetailsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        stopLoading();
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(CompanyDetailsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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
        binding.recyclerView.setVisibility(View.GONE);
        binding.shimmerFrameLayout.setVisibility(View.VISIBLE);
        binding.shimmerFrameLayout.startShimmer();
    }

    private void stopLoading() {
        binding.shimmerFrameLayout.stopShimmer();
        binding.shimmerFrameLayout.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    public String getAllowedBranches(JSONArray array) throws JSONException {
        StringBuilder sb = new StringBuilder(array.getString(0));
        for(int i=1; i<array.length(); i++) {
            sb.append(", ").append(array.getString(i));
        }
        return sb.toString();
    }

    public String getTimeString(Long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(cal.getTime());
    }
}