package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
        showInformation();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void showInformation() {
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url) + "getNotice";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray notices = response.getJSONArray("notices");
                            al = new ArrayList<>();
                            for(int i=0; i< notices.length(); i++) {
                                JSONObject obj = notices.getJSONObject(i);
                                String title = "Not available";
                                if(obj.has("title"))
                                    title = obj.getString("content");

                                Notice notice = new Notice(title, obj.getString("content"), obj.getLong("time"));
                                al.add(notice);
                            }

                            NoticeAdapter adapter = new NoticeAdapter(NoticesActivity.this, al);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(NoticesActivity.this));
                            binding.recyclerView.setAdapter(adapter);

                            binding.animationViewLoading.pauseAnimation();
                            binding.animationViewLoading.setVisibility(View.GONE);
                        }
                        binding.animationViewLoading.pauseAnimation();
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
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

}