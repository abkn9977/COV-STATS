package com.aftab.covid19tracker;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths, tvAffectedCountries;
    TextView tvIndiaCases, tvIndiaToday, tvIndiaRecovered, tvIndiaDeaths, tvIndiaTodayDeaths, tvIndiaActive, tvIndiaCritical;
    SimpleArcLoader simpleArcLoader;
    LinearLayout indiaStats;
    ScrollView scrollView;
    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
        tvAffectedCountries = findViewById(R.id.tvAffectedCountries);

        tvIndiaCases  = findViewById(R.id.tvIndiaCases);
        tvIndiaToday = findViewById(R.id.tvIndiaToday);
        tvIndiaRecovered = findViewById(R.id.tvIndiaRecovered);
        tvIndiaDeaths = findViewById(R.id.tvIndiaDeaths);
        tvIndiaTodayDeaths = findViewById(R.id.tvIndiaTodayDeaths);
        tvIndiaActive = findViewById(R.id.tvIndiaActive);
        tvIndiaCritical = findViewById(R.id.tvIndiaCritical);

        indiaStats = findViewById(R.id.indiaStats);
        simpleArcLoader = findViewById(R.id.loader);
        scrollView = findViewById(R.id.scrollStats);
        pieChart = findViewById(R.id.pieChart);

        getSupportActionBar().setTitle("COVID-19 Statistics");
        fetchIndianData();
        fetchData();
    }

    private void fetchIndianData() {
        //                India section
        String indiaURL = "https://corona.lmao.ninja/v2/countries/india";

        StringRequest indiaRequest = new StringRequest(Request.Method.GET, indiaURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject indiaJsonObject = new JSONObject(response.toString());
                    tvIndiaCases.setText(indiaJsonObject.getString("cases"));
                    tvIndiaRecovered.setText(indiaJsonObject.getString("recovered"));
                    tvIndiaCritical.setText(indiaJsonObject.getString("critical"));
                    tvIndiaActive.setText(indiaJsonObject.getString("active"));
                    tvIndiaToday.setText(indiaJsonObject.getString("todayCases"));
                    tvIndiaDeaths.setText(indiaJsonObject.getString("deaths"));
                    tvIndiaTodayDeaths.setText(indiaJsonObject.getString("todayDeaths"));

                    indiaStats.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    indiaStats.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                indiaStats.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(indiaRequest);
        //                India section
    }

    private void fetchData() {

        String URL = "https://corona.lmao.ninja/v2/all/";
        simpleArcLoader.start();

        StringRequest request = new StringRequest(Request.Method.GET, URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        tvCases.setText(jsonObject.getString("cases"));
                        tvRecovered.setText(jsonObject.getString("recovered"));
                        tvCritical.setText(jsonObject.getString("critical"));
                        tvActive.setText(jsonObject.getString("active"));
                        tvTodayCases.setText(jsonObject.getString("todayCases"));
                        tvTotalDeaths.setText(jsonObject.getString("deaths"));
                        tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                        tvAffectedCountries.setText((jsonObject.getString("affectedCountries")));

                        pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(tvCases.getText().toString()), Color.parseColor("#FFA726")));
                        pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                        pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                        pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                        pieChart.startAnimation();

                        simpleArcLoader.stop();
                        simpleArcLoader.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        simpleArcLoader.stop();
                        simpleArcLoader.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);

                    }

                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            simpleArcLoader.stop();
            simpleArcLoader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
    public void goTrackCountries(View view) {
        startActivity(new Intent(getApplicationContext(), AffectedCountries.class));
    }
}