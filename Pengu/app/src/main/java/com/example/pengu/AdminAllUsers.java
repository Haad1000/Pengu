package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminAllUsers extends AppCompatActivity {
    private Button toUnbanUsers;
    private RecyclerView usersRecyclerView;
    private AllUsersAdapter usersAdapter;
    private List<AllUser> userList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_users);

        bottomNavigationView = findViewById(R.id.admin_navbar);

        bottomNavigationView.setSelectedItemId(R.id.admUsers);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.admSubjects) {
                    Intent intent = new Intent(AdminAllUsers.this, AdminSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admBooking) {
                    Intent intent = new Intent(AdminAllUsers.this, AdminBookingActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admReport) {
                    Intent intent = new Intent(AdminAllUsers.this, AdminDashBoard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admUsers) {
                    //Currently here right now
                    return true;
                }
                else {
                    return true;
                }
            }
        });


        toUnbanUsers =findViewById(R.id.button7);

        usersRecyclerView = findViewById(R.id.UsersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersAdapter = new AllUsersAdapter(userList, new AllUsersAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AllUser userToDelete = userList.get(position);
                sendBanRequestToBackend(userToDelete.getEmail(),true);
            }
        });
        usersRecyclerView.setAdapter(usersAdapter);
        fetchUsers();

        toUnbanUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminAllUsers.this, AdminBannedUsers.class);

                startActivity(intent);
            }
        });
    }



    private void fetchUsers() {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/getAllUsers";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Clear existing data
                        userList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject userObj = response.getJSONObject(i);
                            AllUser user = new AllUser(
                                    userObj.getString("name"),
                                    userObj.getString("emailId"),
                                    userObj.getString("role"),
                                    false // Assuming all fetched users are banned
                            );
                            userList.add(user);
                        }
                        // Notify the adapter of the new data
                        usersAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AdminAllUsers.this, "Error parsing JSON data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e("Volley", "Error: " + error.toString());
                    Toast.makeText(AdminAllUsers.this, "Volley error: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    private void sendBanRequestToBackend(String userEmail, boolean isBanned) {
        String logTag = "AdminAllUsers";
        Log.d(logTag, "sendBanRequestToBackend called");
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        if (token.isEmpty()) {
            Log.e(logTag, "Token is missing.");
            return;
        }
        String action = isBanned ? "banUser" : "unbanUser";
        String url = "http://coms-309-037.class.las.iastate.edu:8080/" + action + "/" +token+"/"+ userEmail;

        Log.d(logTag, "URL: " + url);
        Log.d(logTag, "HTTP Method: POST");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(logTag, "Response: " + response);
                    // Handle successful response
                },
                error -> {
                    Log.e("Volley", "Error: " + error.toString());
                    Toast.makeText(AdminAllUsers.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    // Handle error response
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };



        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    }


