package com.example.pengu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class getTeacherRating extends AppCompatActivity {

    private int teacherid;
    private String baseUrl = "http://coms-309-037.class.las.iastate.edu:8080/getAverageTeacher/";
    private Button getDetailsBtn, goToRateBtn;
    private RatingBar ratingBar;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String getUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_teacher_rating);

//        getDetailsBtn = findViewById(R.id.getDetailsButton);
        goToRateBtn = findViewById(R.id.goToRateButton);
        ratingBar = findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        teacherid = intent.getIntExtra("teacherid", -1);
        Log.d("The Teacher ID: ","" + teacherid);

        getUrl = baseUrl + teacherid;



        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        // Get the integer value from the response
                        int rating = response.getInt("rating");

                        // Update the RatingBar accordingly
                        ratingBar.setRating(rating);

                        //Now make sure that the user is unable to interact with it.
                        ratingBar.setIsIndicator(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley Error", "Error fetching teacher rating: " + error.getMessage());
                }
        });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);

//        getDetailsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Make Volley GET request to fetch teacher rating
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getUrl, null,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    // Get the integer value from the response
//                                    int rating = response.getInt("rating");
//
//                                    // Update the RatingBar accordingly
//                                    ratingBar.setRating(rating);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.e("Volley Error", "Error fetching teacher rating: " + error.getMessage());
//                            }
//                        });
//
//                // Add the request to the RequestQueue
//                requestQueue.add(jsonObjectRequest);
//            }
//        });

//        getDetailsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent rateDetailIntent = new Intent(getTeacherRating.this, MainActivity.class);
//                rateDetailIntent.putExtra("teacherid", teacherid);
//                startActivity(rateDetailIntent);
//
//            }
//        });

        goToRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rateIntent = new Intent(getTeacherRating.this, setTeacherRating.class);
                rateIntent.putExtra("teacherid", teacherid);
                startActivity(rateIntent);
            }
        });
    }
}
