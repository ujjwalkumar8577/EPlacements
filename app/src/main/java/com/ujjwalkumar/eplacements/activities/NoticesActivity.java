package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.adapters.NoticeAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityNoticesBinding;
import com.ujjwalkumar.eplacements.models.Notice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoticesActivity extends AppCompatActivity {

    private ArrayList<Notice> al;
    private ActivityNoticesBinding binding;
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);
        if(user.getString("type", "").equals("admin"))
            binding.buttonAdd.setVisibility(View.VISIBLE);
        else
            binding.buttonAdd.setVisibility(View.GONE);
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.buttonAdd.setOnClickListener(view -> {
            LayoutInflater li = LayoutInflater.from(NoticesActivity.this);
            View promptsView = li.inflate(R.layout.dialog_add_notice, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NoticesActivity.this);
            alertDialogBuilder.setView(promptsView);

            final EditText userInput1 = promptsView.findViewById(R.id.editTextDialogUserInput1);
            final EditText userInput2 = promptsView.findViewById(R.id.editTextDialogUserInput2);

            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Add",
                            (dialog, id) -> {
                                String title = userInput1.getText().toString();
                                String content = userInput2.getText().toString();
                                if(title.equals("") || content.equals(""))
                                    Toast.makeText(NoticesActivity.this, "Fields required", Toast.LENGTH_SHORT).show();
                                else
                                    addNotice(title, content);
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> dialog.cancel());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getWindow().setBackgroundDrawable(AppCompatResources.getDrawable(NoticesActivity.this, R.color.gray));
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
        });
    }

    private void showInformation() {
        startLoading();
        String url = getString(R.string.base_url) + "getNotice";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray notices = response.getJSONArray("notices");
                            al = new ArrayList<>();
                            for(int i=0; i< notices.length(); i++) {
                                JSONObject obj = notices.getJSONObject(i);

                                Notice notice = new Notice(obj.getString("title"), obj.getString("content"), obj.getLong("timestamp"));
                                al.add(notice);
                            }

                            NoticeAdapter adapter = new NoticeAdapter(NoticesActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(NoticesActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(NoticesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        stopLoading();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(NoticesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization", user.getString("token", ""));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void addNotice(String title, String content) {
        String url = getString(R.string.base_url) + "admin/addNotice";
        JSONObject postData = new JSONObject();
        try {
            postData.put("title", title);
            postData.put("content", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONObject obj = response.getJSONObject("notice");
                            Toast.makeText(NoticesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            Notice notice = new Notice(obj.getString("title"), obj.getString("content"), obj.getLong("timestamp"));
                            al.add(0, notice);

                            NoticeAdapter adapter = new NoticeAdapter(NoticesActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(NoticesActivity.this));
                            binding.recyclerView.setAdapter(adapter);
                        }
                        else
                            Toast.makeText(NoticesActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(NoticesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
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
}