package com.example.pengu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class actualTeacherProfile extends AppCompatActivity implements BottomSheetSubjectsAdapter.OnSubjectsUpdatedListener{/////
    private RecyclerView subjectsRecyclerView;
    private SubjectsAdapter adapter;
    private List<Subjects> subjectsList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
//    private Button BackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actual_teacher_profile);

        bottomNavigationView = findViewById(R.id.teach_navbar);

        bottomNavigationView.setSelectedItemId(R.id.teachProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.teachDashboard) {
                    Intent intent = new Intent(actualTeacherProfile.this, TeacherDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachSchedule) {
                    Intent intent = new Intent(actualTeacherProfile.this, TeacherChooseTime.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachInbox) {
                    Intent intent = new Intent(actualTeacherProfile.this, T_InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachProfile) {
                    //Currently Here
                    return true;
                }
                else {
                    return true;
                }
            }
        });


        //String role = "TEACHER";
        ImageButton backButton = findViewById(R.id.backButton);
        subjectsRecyclerView = findViewById(R.id.subjectsRecyclerView);
        subjectsRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Adjust the number of columns as needed

        adapter = new SubjectsAdapter(subjectsList,this); // Corrected adapter initialization
        subjectsRecyclerView.setAdapter(adapter); // Set the adapter here and don't set it again
//        List<Subjects> testData = new ArrayList<>();
//        testData.add(new Subjects(1, "Literature"));
//        testData.add(new Subjects(2, "History"));
//        testData.add(new Subjects(3, "Art"));
//
//        // Add test data to the adapter and notify the adapter to refresh the RecyclerView
//        adapter.setSubjectsList(testData);
//        adapter.notifyDataSetChanged();

        // After setting test data, you can fetch real data


        fetchSubjects(); // Fetch and populate subjects from the backend
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(actualTeacherProfile.this, TeacherDashboard.class);

                startActivity(intent);
            }
        });
    }


    private void fetchSubjects() {
        Log.d("fetchSubjects", "Method is called");
        //probaly use the shared preferences that the backedn repsonded in hte login, will be implemented
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String teacherId = sharedPreferences.getString("userId", ""); // Provide a default value in case it's not found
       // String teacherId = "4"; // Or however you retrieve this from the token/user session
        String url ="http://coms-309-037.class.las.iastate.edu:8080/subjects/" + teacherId;;

        // Create a JSON Array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Clear existing data
                        subjectsList.clear();

                        // Parse the JSON response and create Subject objects
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject subjectObject = response.getJSONObject(i);
                                Subjects subject = new Subjects(
                                        subjectObject.getInt("subjectID"),
                                        subjectObject.getString("subjectName")
                                );
                                subjectsList.add(subject);
                            } catch (JSONException e) {
                                Log.e("JSON Parsing Error", "Error: " + e.getMessage());
                            }
                        }
                        // Notify adapter about data changes
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error: " + error.toString());
                        if (error.networkResponse != null) {
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.e("Volley Error Body", responseBody);
                        }
                    }
                }
        );

        // Add the request to the Volley request queue
        //VolleySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();

        // Add the JsonArrayRequest to the request queue
        queue.add(jsonArrayRequest);

        // Explicitly start the queue (not typically required)
        // queue.start();
    }

    private void showBottomSheet() {
        // Create an instance of the adapter and pass the listener (actualTeacherProfile.this)
        BottomSheetSubjectsAdapter adapter = new BottomSheetSubjectsAdapter(subjectsList, (Context) this, (BottomSheetSubjectsAdapter.OnSubjectsUpdatedListener) this);
        Log.d("does","does this showBottomSheet");
        // ... code to set up and show bottom sheet with this adapter ...
    }
    public void onSubjectsUpdated() {/////////
        fetchSubjects(); // Fetch and populate subjects from the backend
        Log.d("actualTeacherProfile", "Subjects have been updated");
    }
//    public void deleteSubject(int subjectId, int position) {
//        // Make a network request to delete the subject from the server
//        // On successful response, remove the subject from the list and update the adapter
//        subjectsList.remove(position);
//        adapter.notifyItemRemoved(position);
//        adapter.notifyItemRangeChanged(position, subjectsList.size());
//    }

    public void deleteSubject(int subjectId, int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String teacherId = sharedPreferences.getString("userId", ""); // Provide a default value in case it's not found
       // String teacherId = "4";

        String url = "http://coms-309-037.class.las.iastate.edu:8080/delete/"+teacherId+"/" + subjectId;

        // Create a new Volley string request
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Handle response
                    Toast.makeText(this, "Subject deleted successfully", Toast.LENGTH_SHORT).show();

                    // Update your RecyclerView
                    subjectsList.remove(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, subjectsList.size());

                }, error -> {
            // Handle error
            Toast.makeText(this, "Failed to delete subject", Toast.LENGTH_SHORT).show();
            Log.e("deleteSubject", "Error: " + error.toString());
        }) {

            // This is where you would set up your request headers if needed
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Example: headers.put("Authorization", "Bearer " + authToken);
                return headers;
            }
        };

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
