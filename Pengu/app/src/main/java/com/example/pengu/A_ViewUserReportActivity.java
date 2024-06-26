package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class A_ViewUserReportActivity extends AppCompatActivity {

//    private TextView reportTitle, reportDescription;
    private String baseUrl = "http://coms-309-037.class.las.iastate.edu:8080/getReports/";
    private String updateBaseUrl = "http://coms-309-037.class.las.iastate.edu:8080/updateReport/";
    private int reportid;
    private String getReportUrl, authToken, updateUrl;
    private Button updateStatusBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_report);

//        reportTitle = findViewById(R.id.textReportTitle);
//        reportDescription = findViewById(R.id.textReportDescription);
        updateStatusBtn = findViewById(R.id.updateStatusButton);

        Intent intent = getIntent();
        reportid = intent.getIntExtra("reportid", -1);
        Log.e("Report ID:", "" + reportid);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        authToken = sharedPreferences.getString("AuthToken", "");
        Log.e("The user Token is: ", authToken);

        getReportUrl = baseUrl + reportid;

        updateUrl = updateBaseUrl + reportid;

//        performVolleyGetRequest(getReportUrl);

        updateStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });

    }

//    private void performVolleyGetRequest(String url) {
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                            // Since the response is an array, get the first object
//                            JSONObject reportObject = response.getJSONObject(0);
//
//                            // Parse the response and update UI
//                            String title = reportObject.getString("title");
//                            String description = reportObject.getString("description");
//
//                            // Update TextViews with report details
//                            reportTitle.setText(title);
//                            reportDescription.setText(description);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
//                        Log.e("Volley Error", "Error occurred: " + error.getMessage());
//                    }
//                });
//
//        // Add the request to the RequestQueue
//        queue.add(jsonArrayRequest);
//    }
    private void updateStatus() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject(); // Create an empty JSON object for PUT request

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, updateUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle success response
                        try {
                            String message = response.getString("message");
                            Log.d("Volley Response", message);
                            // Handle the success message here
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Log.e("Volley Error", "Error occurred: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                // Set headers if required (like Authorization header)
                Map<String, String> headers = new HashMap<>();
                // Add your headers here if needed
                return headers;
            }
        };

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }

}
