package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class reportTeacherActivity extends AppCompatActivity {

    private EditText titletxt, descriptiontxt;
    private Button submitBtn;
    private TextView gotorate;
    private int userid;
    private String baseUrl = "http://coms-309-037.class.las.iastate.edu:8080/createReport/";
    private String reportUrl, authToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_teacher);

        titletxt = findViewById(R.id.reportTitle);
        descriptiontxt = findViewById(R.id.reportDescription);
        submitBtn = findViewById(R.id.submitButton);
        gotorate = findViewById(R.id.ratingPage);

        Intent intent = getIntent();
        userid = intent.getIntExtra("teacherID", -1);
        Log.e("Teacher ID to report on new page: ", "" + userid);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        authToken = sharedPreferences.getString("AuthToken", "");
        Log.e("The user Token is: ", authToken);

        reportUrl = baseUrl + authToken;
        Log.e("The new URL being used is: ",reportUrl);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performReportSubmission();
            }
        });

        gotorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ratingIntent = new Intent(reportTeacherActivity.this, getTeacherRating.class);
                ratingIntent.putExtra("teacherid", userid);
                startActivity(ratingIntent);
            }
        });
    }
    private void performReportSubmission() {
        String title = titletxt.getText().toString().trim();
        String description = descriptiontxt.getText().toString().trim();

        if (!title.isEmpty() && !description.isEmpty()) {
            JSONObject requestBody = new JSONObject();
//            "teacherReportedID": "2",
//            "title": "REALLY BAD",
//            "description": "SMTH"
            try {
                requestBody.put("teacherReportedID", userid);
                requestBody.put("title", title);
                requestBody.put("description", description);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
            RequestQueue requestQueue = volleySingleton.getRequestQueue();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, reportUrl, requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                if (message.equals("success")) {
                                    Log.d("Report Status", "Success");
                                } else {
                                    Log.d("Report Status", "Failure");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error", error.getMessage());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(request);

        }
    }
}
