package com.ujjwalkumar.eplacements.activities.common;

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
    private Pie pie1, pie2;
    private ActivityStatisticsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EPlacementsUtil.checkInternetConnection(this);

        year = binding.spinnerYear.getSelectedItem().toString();
        pie1 = AnyChart.pie();
        pie2 = AnyChart.pie();

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

                            ArrayList<DataEntry> pieData1 = new ArrayList<>();
                            ArrayList<DataEntry> pieData2 = new ArrayList<>();
                            ArrayList<DataTableRow> rows = new ArrayList<>();
                            for(int i=0; i< arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                ArrayList<String> rowData = new ArrayList<>();
                                for(int j=0; j<obj.names().length(); j++) {
                                    rowData.add(obj.get(headerData1.get(j)).toString());
                                }
                                rows.add(new DataTableRow(rowData));
                                pieData1.add(new ValueDataEntry(obj.getString("Branch"), obj.getDouble("% Placement")));
                                pieData2.add(new ValueDataEntry(obj.getString("Branch"), obj.getDouble("Avg CTC (in LPA)")));
                            }

                            binding.dataTable.setHeader(new DataTableHeader(headerData1, headerData2));
                            binding.dataTable.setRows(rows);
                            binding.dataTable.inflate(StatisticsActivity.this);

                            pie1.data(pieData1);
                            pie1.title("% Placement in year " + year);
                            pie1.labels().position("outside");
                            pie1.legend().title().enabled(true);
                            pie1.legend().title()
                                    .text("Branch")
                                    .padding(0d, 0d, 10d, 0d);
                            pie1.legend()
                                    .position("center-bottom")
                                    .itemsLayout(LegendLayout.HORIZONTAL)
                                    .align(Align.CENTER);
                            binding.chartView1.setChart(pie1);

                            pie2.data(pieData2);
                            pie2.title("Avg. CTC in year " + year);
                            pie2.labels().position("outside");
                            pie2.legend().title().enabled(true);
                            pie2.legend().title()
                                    .text("Branch")
                                    .padding(0d, 0d, 10d, 0d);
                            pie2.legend()
                                    .position("center-bottom")
                                    .itemsLayout(LegendLayout.HORIZONTAL)
                                    .align(Align.CENTER);
                            binding.chartView2.setChart(pie2);

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