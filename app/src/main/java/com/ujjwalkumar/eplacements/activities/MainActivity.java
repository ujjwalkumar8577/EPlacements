package com.ujjwalkumar.eplacements.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ujjwalkumar.eplacements.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final Timer timer = new Timer();
    private SharedPreferences user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = getSharedPreferences("user", Activity.MODE_PRIVATE);

        TimerTask Splash = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    String token = user.getString("token", "");
                    String type = user.getString("type", "");
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_VIEW);
                    if(token.equals(""))
                        in.setClass(getApplicationContext(), LoginActivity.class);
                    else if(type.equals("admin"))
                        in.setClass(getApplicationContext(), AdminHomeActivity.class);
                    else
                        in.setClass(getApplicationContext(), HomeActivity.class);
                    startActivity(in);
                    finish();
                });
            }
        };
        timer.schedule(Splash,1500);
    }
}