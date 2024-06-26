package com.example.pengu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminBannedUsers extends AppCompatActivity {
    private RecyclerView usersRecyclerView;
    private AllUsersAdapter usersAdapter;
    private List<AllUser> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_banned_users);


        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will just close the current activity and go back to the previous one in the stack
                finish();
            }
        });
        usersRecyclerView = findViewById(R.id.UsersRecyclerView); // make sure this ID is correct
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new AllUsersAdapter(userList, new AllUsersAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AllUser userToDelete = userList.get(position);
                sendBanRequestToBackend(userToDelete.getEmail(),false);  // Adjust this if needed
            }
        });
        usersRecyclerView.setAdapter(usersAdapter);
        fetchUsers();  // This method should be correctly fetching the banned users
    }

    private void fetchUsers() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        if (token.isEmpty()) {
            Log.e("CreateBooking", "Auth token is missing.");
            return;
        }
        //String url = "http://coms-309-037.class.las.iastate.edu:8080/getBannedUsers"; // Adjusted URL
        String url ="http://coms-309-037.class.las.iastate.edu:8080/getAllBannedUsers/"+ token;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        userList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject userObj = response.getJSONObject(i);
                            AllUser user = new AllUser(
                                    userObj.getString("name"),
                                    userObj.getString("emailId"),
                                    userObj.getString("role"),
                                    true // Assuming all fetched users are banned
                            );
                            userList.add(user);
                        }
                        usersAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error parsing JSON data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Volley error: " + error.toString(), Toast.LENGTH_LONG).show()
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
                    Toast.makeText(AdminBannedUsers.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
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