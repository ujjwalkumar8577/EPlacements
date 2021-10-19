package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ujjwalkumar.eplacements.adapters.KeyValueAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityCompanyDetailsBinding;

import java.util.ArrayList;

public class CompanyDetailsActivity extends AppCompatActivity {

    private ActivityCompanyDetailsBinding binding;
    ArrayList<Pair<String, String>> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadRecyclerView();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void loadRecyclerView() {
        al = new ArrayList<>();
        al.add(new Pair<>("Name", "Google"));
        al.add(new Pair<>("Job Profile", "SDE I"));
        al.add(new Pair<>("Job Location", "Mumbai"));
        al.add(new Pair<>("Job Description", "40 LPA"));
        al.add(new Pair<>("Provision for PPO", "Yes - Based on performance"));
        al.add(new Pair<>("Process", "Resume>>Test>>Interview"));
        al.add(new Pair<>("CTC", "40LPA"));
        al.add(new Pair<>("Allowed branches", "CSE, IT, ECE, EE"));
        al.add(new Pair<>("Registration Deadline", "12/10/2021 11:59 PM"));
        al.add(new Pair<>("Company Link", ""));

        KeyValueAdapter adapter = new KeyValueAdapter(this, al);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}