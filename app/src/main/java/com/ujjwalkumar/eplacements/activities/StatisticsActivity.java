package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.ujjwalkumar.eplacements.R;
import com.ujjwalkumar.eplacements.databinding.ActivityStatisticsBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private ArrayList<DataEntry> data1;
    private String year;
    private Pie pie;
    private ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        year = binding.spinnerYear.getSelectedItem().toString();
        pie = AnyChart.pie();

        binding.spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = binding.spinnerYear.getSelectedItem().toString();
                showInformation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void showInformation() {
        binding.animationViewLoading.setVisibility(View.VISIBLE);
        binding.animationViewLoading.playAnimation();
        String url = getString(R.string.base_url) + "getStats/?year=" + year;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if(response.getBoolean("success")) {
                            JSONArray arr = response.getJSONObject("stats").getJSONArray("data");
                            data1 = new ArrayList<>();
                            for(int i=0; i< arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                data1.add(new ValueDataEntry(obj.getString("Branch"), obj.getDouble("% Placement")));
                            }

                            pie.data(data1);
                            pie.title("% Placement in year " + year);
                            pie.labels().position("outside");
                            pie.legend().title().enabled(true);
                            pie.legend().title()
                                    .text("Branch")
                                    .padding(0d, 0d, 10d, 0d);
                            pie.legend()
                                    .position("center-bottom")
                                    .itemsLayout(LegendLayout.HORIZONTAL)
                                    .align(Align.CENTER);
                            binding.chartView.setChart(pie);
                        }
                        else
                            Toast.makeText(StatisticsActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                    } catch (Exception e) {
                        binding.animationViewLoading.pauseAnimation();
                        binding.animationViewLoading.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(StatisticsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show()){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}