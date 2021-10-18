package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ujjwalkumar.eplacements.adapters.ContactAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityContactsBinding;
import com.ujjwalkumar.eplacements.models.Contact;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ArrayList<Contact> al;
    private ActivityContactsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadRecyclerView();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });
    }

    private void loadRecyclerView() {
        al = new ArrayList<>();
        al.add(new Contact("Training & Placement Office", "Administration", "", "0532-2545677", "placement2k23@gmail.com"));
        al.add(new Contact("Harsh Gupta", "TPR", "B Tech. - CHE", "9898989898", "user1@gmail.com"));
        al.add(new Contact("Aditya Gupta", "TPR", "B Tech. - CSE", "9898989898", "user2@gmail.com"));
        al.add(new Contact("Saksham Gupta", "TPR", "B Tech. - ME", "9898989898", "user3@gmail.com"));
        al.add(new Contact("Chintu Gupta", "TPR", "B Tech. - PIE", "9898989898", "user4@gmail.com"));
        al.add(new Contact("Sonu Gupta", "TPR", "B Tech. - IT", "9898989898", "user4@gmail.com"));

        ContactAdapter adapter = new ContactAdapter(this, al);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}