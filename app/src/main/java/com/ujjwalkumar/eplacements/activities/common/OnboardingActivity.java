package com.ujjwalkumar.eplacements.activities.common;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.activities.student.LoginActivity;
import com.ujjwalkumar.eplacements.databinding.ActivityOnboardingBinding;

import java.util.ArrayList;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private ArrayList<PaperOnboardingPage> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        al = new ArrayList<>();
        al.add(new PaperOnboardingPage("Registrations", "Easily register for companies", R.color.onboardBg1, R.drawable.outline_corporate_fare_white_48dp, R.drawable.forward));
        al.add(new PaperOnboardingPage("Notifications", "Get push notifications", R.color.onboardBg2, R.drawable.outline_notifications_white_48dp, R.drawable.forward));
        al.add(new PaperOnboardingPage("Experiences", "Learn from previous experiences", R.color.onboardBg1, R.drawable.outline_groups_white_48dp, R.drawable.forward));
//        al.add(new PaperOnboardingPage("Welcome", "Login or register to continue", Color.BLACK, R.drawable.outline_verified_user_white_48dp, R.drawable.forward));

        FragmentManager fragmentManager = getSupportFragmentManager();
        final PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(al);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, paperOnboardingFragment);
        fragmentTransaction.commit();

        paperOnboardingFragment.setOnRightOutListener(() -> {
            startActivity(new Intent(OnboardingActivity.this, LoginActivity.class));
        });
    }
}