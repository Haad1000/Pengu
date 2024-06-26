package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherDashboard extends AppCompatActivity {

    private RecyclerView allBookingItem;
    private String BASE_URL = "http://coms-309-037.class.las.iastate.edu:8080/getBookingsByTeacher/";
    private String authToken, getBookings_Url;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_schedule);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        authToken = sharedPreferences.getString("AuthToken", "");
        Log.d("The user Token is: ", authToken);

        bottomNavigationView = findViewById(R.id.teach_navbar);

        bottomNavigationView.setSelectedItemId(R.id.teachDashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.teachDashboard) {
                    //Currently here
                    return true;
                }
                else if (item.getItemId() == R.id.teachSchedule) {
                    Intent intent = new Intent(TeacherDashboard.this, TeacherChooseTime.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachInbox) {
                    Intent intent = new Intent(TeacherDashboard.this, T_InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachProfile) {
                    Intent intent = new Intent(TeacherDashboard.this, actualTeacherProfile.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        //Add the rest here, particularly RecycleView
        allBookingItem = findViewById(R.id.bookingItems);

        allBookingItem.setLayoutManager(new LinearLayoutManager(this));

        getBookings_Url = BASE_URL + authToken;

        fetchBookings(getBookings_Url);
    }

    private void fetchBookings(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<StudentBookingItem> bookingList = new ArrayList<>();

                        try {
                            // Iterate over the JSON array and extract booking data
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject bookingObj = response.getJSONObject(i);

                                String subjectName = bookingObj.getJSONObject("subject").getString("subjectName");
                                String teacherName = bookingObj.getJSONObject("teacher").getString("name");
                                String date = bookingObj.getString("date");
                                String startTime = bookingObj.getString("startTime");

                                // Create a StudentBookingItem and add to the list
                                bookingList.add(new StudentBookingItem(subjectName, teacherName, date, startTime));
                            }

                            // Create an adapter and set it to the RecyclerView
                            StudentBookingAdapter adapter = new StudentBookingAdapter(bookingList);
                            allBookingItem.setAdapter(adapter);

                        } catch (JSONException e) {
                            Log.e("BookingError", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("JSON Array Volley", error.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

}

