package com.ujjwalkumar.eplacements.activities.admin;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ujjwalkumar.eplacements.adapters.SelectedStudentAdapter;
import com.ujjwalkumar.eplacements.databinding.ActivityAdminAnnounceResultBinding;

import java.util.ArrayList;

public class AdminAnnounceResultActivity extends AppCompatActivity {

    private ActivityAdminAnnounceResultBinding binding;
    private ArrayList<String> companies = new ArrayList<>();
    private ArrayList<Pair<String, String>> allStudents = new ArrayList<>();
    private ArrayList<Pair<String, String>> selectedStudents = new ArrayList<>();
    private ArrayList<String> al = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAnnounceResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        companies.add("Google");
        companies.add("Microsoft");
        companies.add("Facebook");
        companies.add("Amazon");
        allStudents.add(new Pair<>("Aman Gupta", "20197698"));
        allStudents.add(new Pair<>("Anuj Mishra", "20191698"));
        allStudents.add(new Pair<>("Vishal Verma", "20197498"));
        allStudents.add(new Pair<>("Utkarsh Singh", "20191098"));
        allStudents.add(new Pair<>("Surbhi Meenal", "20197665"));

        SelectedStudentAdapter adapter = new SelectedStudentAdapter(AdminAnnounceResultActivity.this, selectedStudents);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminAnnounceResultActivity.this));
        binding.recyclerView.setAdapter(adapter);

        binding.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String key = (String) adapterView.getItemAtPosition(i);
                int pos = al.indexOf(key);
                if(selectedStudents.indexOf(allStudents.get(pos))<0) {
                    selectedStudents.add(allStudents.get(pos));
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                }
                else {
                    Toast.makeText(AdminAnnounceResultActivity.this, "Already selected", Toast.LENGTH_SHORT).show();
                }
                binding.autoCompleteTextView.setText("");
            }
        });
        
        binding.buttonDone.setOnClickListener(view -> {
            if(selectedStudents.size()>0) {
                addResult();
            }
            else {
                Toast.makeText(AdminAnnounceResultActivity.this, "No Student selected", Toast.LENGTH_SHORT).show();
            }
        });

        initializeFields();
    }

    private void initializeFields() {
        binding.animationViewLoading.pauseAnimation();
        binding.animationViewLoading.setVisibility(View.GONE);

        binding.spinnerCompany.setAdapter(new ArrayAdapter<String>(AdminAnnounceResultActivity.this, android.R.layout.simple_list_item_1, companies));

        al = new ArrayList<>();
        for(Pair<String, String> pair: allStudents) {
            al.add(pair.second + " - " + pair.first);
        }
        //Creating the instance of ArrayAdapter containing list of customers
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_selectable_list_item, al);

        //Getting the instance of AutoCompleteTextView
        binding.autoCompleteTextView.setThreshold(2);                       //will start working from first character
        binding.autoCompleteTextView.setAdapter(adapter);                   //setting the adapter data into the AutoCompleteTextView
    }
    
    private void addResult() {
        
    }
}