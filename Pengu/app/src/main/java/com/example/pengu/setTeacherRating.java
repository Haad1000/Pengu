package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class setTeacherRating extends AppCompatActivity {

    private String baseCreateUrl = "http://coms-309-037.class.las.iastate.edu:8080/rate/";
    private String createUrl;
    private int teacherId;
    private RatingBar communicateRate, teachRate, recommendRate;
    private Button createBtn, viewMyRatingsBtn;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_teacher_rating);

        communicateRate = findViewById(R.id.communicationRatingBar);
        teachRate = findViewById(R.id.teachingRatingBar);
        recommendRate = findViewById(R.id.recommendationRatingBar);
        createBtn = findViewById(R.id.updateButton);
//        viewMyRatingsBtn = findViewById(R.id.viewMyRatingsButton);

//        viewMyRatingsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent viewRatingsIntent = new Intent(setTeacherRating.this, S_ViewMyRatingsActivity.class);
//                Intent viewRatingsIntent = new Intent(setTeacherRating.this, EditRating.class);
//                startActivity(viewRatingsIntent);
//            }
//        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");

        Intent intent = getIntent();
        teacherId = intent.getIntExtra("teacherid", -1);
        Log.d("The Teacher ID: ", String.valueOf(teacherId));

        createUrl = baseCreateUrl + token;

        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRating();
            }
        });
    }

    private void createRating() {
        float communicateRateValue = communicateRate.getRating();
        float teachRateValue = teachRate.getRating();
        float recommendRateValue = recommendRate.getRating();

        int roundedCommunicateRate = (int) Math.ceil(communicateRateValue);
        int roundedTeachRate = (int) Math.ceil(teachRateValue);
        int roundedRecommendRate = (int) Math.ceil(recommendRateValue);

        String Tid = Integer.toString(teacherId);
        String c = Integer.toString(roundedCommunicateRate);
        String t = Integer.toString(roundedTeachRate);
        String r = Integer.toString(roundedRecommendRate);

        Log.d("Test Conversion", "Communicate Rate: " + c);
        Log.d("Test Conversion", "Teach Rate: " + t);
        Log.d("Test Conversion", "Recommend Rate: " + r);


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("teacherId", Tid);
            jsonObject.put("communicationRating", c);
            jsonObject.put("teachingRating", t);
            jsonObject.put("recommendRating", r);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JSON Object", jsonObject.toString());

        VolleySingleton volleySingleton = VolleySingleton.getInstance(this);
        RequestQueue requestQueue1 = volleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createUrl, jsonObject,
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

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, createUrl, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Response",response.toString());
//                try {
//                    if (response.has("message")) {
//                        String message = response.getString("message");
//                        if ("success".equals(message)) {
//                            Log.d("Report Status", "Success");
//                        } else {
//                            Log.d("Report Status", "Failure");
//                        }
//                    } else {
//                        Log.e("JSON Parsing Error", "Response does not contain 'message' field");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.e("JSON Parsing Error", "Error parsing JSON response: " + e.getMessage());
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("Error Response",error.toString());
//                // Log the network response for debugging
//                if (error.networkResponse != null && error.networkResponse.data != null) {
//                    String errorData = new String(error.networkResponse.data);
//                    Log.e("Volley Error", "Error response: " + errorData);
//                }
//                Log.e("Volley Error", error.getMessage());
//            }
//        }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//        requestQueue.add(jsonObjectRequest);
    }
}
