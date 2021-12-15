package com.ujjwalkumar.eplacements.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
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
import com.ujjwalkumar.eplacements.utilities.EPlacementsUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class StatisticsActivity extends AppCompatActivity {

    private String year;
    private Pie pie;
    private ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EPlacementsUtil.checkInternetConnection(this);

        year = binding.spinnerYear.getSelectedItem().toString();
        pie = AnyChart.pie();

        binding.imageViewBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        binding.spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = binding.spinnerYear.getSelectedItem().toString();
                ((TextView)view).setTextColor(getResources().getColor(R.color.gray));
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

                            ArrayList<String> headerData1 = new ArrayList<>();
                            ArrayList<Integer> headerData2 = new ArrayList<>();
                            JSONObject tmp0 = arr.getJSONObject(0);
                            for(int i=0; i<tmp0.names().length(); i++) {
                                headerData1.add(tmp0.names().getString(i));
                                headerData2.add(1);
                            }
                            headerData2.set(0, 2);

                            ArrayList<DataEntry> pieData = new ArrayList<>();
                            ArrayList<DataTableRow> rows = new ArrayList<>();
                            for(int i=0; i< arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                ArrayList<String> rowData = new ArrayList<>();
                                for(int j=0; j<obj.names().length(); j++) {
                                    rowData.add(obj.get(headerData1.get(j)).toString());
                                }
                                rows.add(new DataTableRow(rowData));
                                pieData.add(new ValueDataEntry(obj.getString("Branch"), obj.getDouble("% Placement")));
                            }

                            binding.dataTable.setHeader(new DataTableHeader(headerData1, headerData2));
                            binding.dataTable.setRows(rows);
                            binding.dataTable.inflate(StatisticsActivity.this);

                            pie.data(pieData);
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

                            binding.scrollView.fullScroll(View.FOCUS_UP);
                            binding.horizontalScrollView.fullScroll(View.FOCUS_LEFT);
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