package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ujjwalkumar.eplacements.adapters.RegisteredCompanyAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityRegisteredCompaniesBinding;
import com.ujjwalkumar.eplacements.models.RegisteredCompany;

import java.util.ArrayList;

public class RegisteredCompaniesActivity extends AppCompatActivity {

    private ArrayList<RegisteredCompany> al;
    private ActivityRegisteredCompaniesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredCompaniesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadRecyclerView();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void loadRecyclerView() {
        al = new ArrayList<>();
        al.add(new RegisteredCompany("1", "Google Inc", "SDE I", "40 LPA", "21/10/2021 11:59 PM"));
        al.add(new RegisteredCompany("2","Amazon", "SDE I", "32 LPA", "10/10/2021 11:59 PM"));
        al.add(new RegisteredCompany("3","Myntra", "Business Analyst", "12 LPA", "26/10/2021 11:59 PM"));
        al.add(new RegisteredCompany("4","Facebook", "Technology Analyst", "18 LPA", "18/10/2021 11:59 PM"));
        al.add(new RegisteredCompany("5","Tata Motors", "Junior Engineer", "9 LPA", "29/10/2021 11:59 PM"));
        al.add(new RegisteredCompany("6","Trilogy Innovations", "Software Developer", "30.5 LPA", "12/10/2021 11:59 PM"));

        RegisteredCompanyAdapter adapter = new RegisteredCompanyAdapter(this, al);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}