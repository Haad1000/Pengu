package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AdminSubjectsActivity extends AppCompatActivity {
    private RecyclerView subjectsRecyclerView;
    private AdminSubjectAdapter subjectAdapter;
    private List<AdminSubject> subjectList;
    private EditText subjectNameEditText;
    private Button addSubjectButton;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_subjects);
        bottomNavigationView = findViewById(R.id.admin_navbar);

        bottomNavigationView.setSelectedItemId(R.id.admReport);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.admSubjects) {
                    Intent intent = new Intent(AdminSubjectsActivity.this, AdminSubjectsActivity.class);
                    startActivity(intent);

                    return true;
                }
                else if (item.getItemId() == R.id.admBooking) {
                    Intent intent = new Intent(AdminSubjectsActivity.this, AdminBookingActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.admReport) {
                    //Currently here right now
                    return true;
                }
                else if (item.getItemId() == R.id.admUsers) {
                    Intent intent = new Intent(AdminSubjectsActivity.this, AdminAllUsers.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        subjectNameEditText = findViewById(R.id.subjectNameEditText);
        addSubjectButton = findViewById(R.id.addSubjectButton);

       // fetchUserRole();
//
        subjectList = new ArrayList<>();
//
//        subjectList.add(new AdminSubject(1,"Mathematics"));
//        subjectList.add(new AdminSubject(2,"Physics"));
//        subjectList.add(new AdminSubject(3,"Chemistry"));
//        subjectList.add(new AdminSubject(4,"Biology"));

        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        subjectAdapter = new AdminSubjectAdapter(subjectList, this);

        subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectsRecyclerView.setAdapter(subjectAdapter);

        fetchSubjects();

        addSubjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectName = subjectNameEditText.getText().toString();
                if (!subjectName.isEmpty()) {
                    createSubject(subjectName);
                } else {
                    Toast.makeText(AdminSubjectsActivity.this, "Please enter a subject name", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    public void fetchSubjects() {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/printSubject";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject subjectObject = response.getJSONObject(i);
                            int id = subjectObject.getInt("subjectID");
                            String name = subjectObject.getString("subjectName");
                            AdminSubject subject = new AdminSubject(id, name);
                            subjectList.add(subject);
                        }
                        subjectAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle error
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                });

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }


    public void updateSubjectOnServer(AdminSubject subject, int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        String url = "http://coms-309-037.class.las.iastate.edu:8080/updateSubject/"+token; // Replace with your actual URL

        JSONObject putParams = new JSONObject();
        try {
            putParams.put("subjectID", subject.getID());
            putParams.put("subjectName", subject.getName());
//            putParams.put("subjectID", 1);
//           putParams.put("subjectName", "chemm");
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating update parameters", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, putParams,
                response -> {
                    try {
                        if(response.getString("message").equals("success")) {
                            // Update the RecyclerView with the new data.
                            runOnUiThread(() -> {
                                subjectAdapter.notifyItemChanged(position);
                                Toast.makeText(this, "Subject updated successfully", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing update response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        try {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            JSONObject data = new JSONObject(responseBody);
                            String message = data.optString("message", "Unknown error");
                            Toast.makeText(AdminSubjectsActivity.this, "Error updating subject: " + message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminSubjectsActivity.this, "Error parsing error response", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Server error or no server response", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    public void deleteSubjectFromServer(int subjectId, int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        String url = "http://coms-309-037.class.las.iastate.edu:8080/deleteSubject/"+subjectId +"/"+ token;
        Log.e("DeleteSubject", "subkectid: " + subjectId);

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    Log.d("DeleteSubject", "Response: " + response);
                    runOnUiThread(() -> {
                        subjectAdapter.deleteSubject(position);
                        Toast.makeText(this, "Subject deleted successfully", Toast.LENGTH_SHORT).show();
                    });
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorResponse = new String(error.networkResponse.data);
                        Log.e("DeleteSubject", "Error response: " + errorResponse);
                        Toast.makeText(this, "Error deleting subject: " + errorResponse, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("DeleteSubject", "Unknown error while deleting subject");
                        Toast.makeText(this, "Unknown error while deleting subject", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token); // Assuming you're using Bearer tokens
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(deleteRequest);
    }
    public void createSubject(String subjectName) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        String url = "http://coms-309-037.class.las.iastate.edu:8080/createSubject/" + token;
        JSONObject postParams = new JSONObject();
        try {
            postParams.put("subjectName", subjectName);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating post parameters", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postParams,
                response -> {
                    Log.d("CreateSubject", "Response: " + response.toString()); // Log the entire response
                    try {
                        String message = response.getString("message");
                        if ("success".equals(message)) {
                            Toast.makeText(this, "Subject created successfully", Toast.LENGTH_SHORT).show();
                            //AdminSubject newSubject = parseSubjectFromResponse(response);
                            //Log.d("CreateSubject", "New Subject: ID=" + newSubject.getID() + " Name=" + newSubject.getName()); // Log the new subject details
                            //addSubjectToRecyclerView(newSubject);
                            // Handle successful subject creation here, e.g., update your adapter/list
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing post response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                    Toast.makeText(this, "Error posting subject", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                // If there are any other headers, they go here
                return headers;
            }
        };

        // Add the request to the queue
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        refreshActivity();
    }


    public void addSubjectToRecyclerView(AdminSubject newSubject) {
        subjectList.add(newSubject); // Add the new subject to your data set
        subjectAdapter.notifyItemInserted(subjectList.size() - 1); // Notify the adapter that an item was inserted at the last position
        subjectsRecyclerView.scrollToPosition(subjectList.size() - 1); // Optional: scroll to the new item
    }



    // Method to parse the response and create a new AdminSubject
//    private AdminSubject parseSubjectFromResponse(JSONObject response) {
//        int id = response.optInt("subjectID", -1); // Provide a default value
//        String name = response.optString("subjectName", "No Na"); // Provide a default value
//
//        // Now log the values to check if they are correct
//        Log.d("CreateSubject", "Parsed ID: " + id + ", Name: " + name);
//
//        return new AdminSubject(id, name);
//    }
    private void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    // Method to add a new subject to the RecyclerView and notify the adapter



//    public void fetchUserRole() {
//        Log.d("UserRole", "fetchUserRole() called"); // Log to confirm the method is called
//        String url = "http://coms-309-037.class.las.iastate.edu:8080/getUser";
//        Log.d("bla","link bla done");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                response -> {
//                    try {
//                        Log.d("bla","try entered bla");
//                        String role = response.getString("role");
//                        Log.d("UserRole", "User role: " + role); // Log the role
//                        Toast.makeText(this, "User role: " + role, Toast.LENGTH_LONG).show(); // Show the role in a toast
//                        Log.d("UserRole", "Response: " + response.toString());
//                    } catch (JSONException e) {
//                        Log.d("bla","catch entered bla");
//                        Log.e("UserRole", "JSON parsing error", e); // Log JSON parsing errors
//                    }
//                },
//                error -> {
//                    Log.d("bla","simply error bla");
//                    Log.e("UserRole", "Volley error", error); // Log Volley errors
//                    Toast.makeText(this, "Error fetching user role", Toast.LENGTH_LONG).show();
//                });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }


}