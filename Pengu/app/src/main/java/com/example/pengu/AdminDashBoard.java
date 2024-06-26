package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class AdminDashBoard extends AppCompatActivity {
    private Button toBookings;


    private Button reportsBtn, deleteReportsBtn, takeMeToBookings, takeMeToSubjects, deleteRateBtn,toAllusers;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        bottomNavigationView = findViewById(R.id.admin_navbar);

        bottomNavigationView.setSelectedItemId(R.id.admReport);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.admSubjects) {
                    Intent intent = new Intent(AdminDashBoard.this, AdminSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admBooking) {
                    Intent intent = new Intent(AdminDashBoard.this, AdminBookingActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admReport) {
                    //Currently here right now
                    return true;
                }
                else if (item.getItemId() == R.id.admUsers) {
                    Intent intent = new Intent(AdminDashBoard.this, AdminAllUsers.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        reportsBtn = findViewById(R.id.button4);
        deleteReportsBtn = findViewById(R.id.button5);
        deleteRateBtn = findViewById(R.id.deleteRateButton);
        takeMeToBookings = findViewById(R.id.button3);
        toAllusers = findViewById(R.id.button6);
        takeMeToSubjects = findViewById(R.id.button2);


        reportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDashBoard.this, A_ViewReportsActivity.class);

                startActivity(intent);
            }
        });

        deleteReportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve token from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
                String token = sharedPreferences.getString("AuthToken", "");

                // Create Volley request queue
                RequestQueue queue = Volley.newRequestQueue(AdminDashBoard.this);

                // Define the URL for DELETE request
                String url = "http://coms-309-037.class.las.iastate.edu:8080/deleteAllReports/" + token;

                // Create DELETE request
                StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle response
                                // For example, show a toast message
                                Toast.makeText(AdminDashBoard.this, "Reports deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        // For example, show a toast message
                        Toast.makeText(AdminDashBoard.this, "Error deleting reports: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(deleteRequest);
            }
        });
//        takeMeToBookings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(this, AdminBookingActivity.class);
//
//                startActivity(intent);
//            }
//        });
        takeMeToBookings.setOnClickListener(view -> {

            Intent intent = new Intent(this, AdminBookingActivity.class);
            startActivity(intent);
        });
        toAllusers.setOnClickListener(view -> {

            Intent intent = new Intent(this, AdminAllUsers.class);
            startActivity(intent);
        });

        takeMeToSubjects.setOnClickListener(view -> {

            Intent intent = new Intent(this, AdminSubjectsActivity.class);
            startActivity(intent);
        });

        deleteRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve token from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
                String token = sharedPreferences.getString("AuthToken", "");

                // Create Volley request queue
                RequestQueue queue = Volley.newRequestQueue(AdminDashBoard.this);

                // Define the URL for DELETE request
                String url = "http://coms-309-037.class.las.iastate.edu:8080/deleteRating/" + token;

                // Create DELETE request
                StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle response
                                // For example, show a toast message
                                Toast.makeText(AdminDashBoard.this, "Reports deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        // For example, show a toast message
                        Toast.makeText(AdminDashBoard.this, "Error deleting reports: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(deleteRequest);
            }
        });

    }
}