package com.example.pengu;

import android.content.Intent;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSubjectsActivity extends AppCompatActivity {

    private RecyclerView subjectView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String url = "http://coms-309-037.class.las.iastate.edu:8080/printSubject";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_subjects);

        bottomNavigationView = findViewById(R.id.student_navbar);

        bottomNavigationView.setSelectedItemId(R.id.stuDashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.stuDashboard) {
                    //currently in here
                    return true;
                }
                else if (item.getItemId() == R.id.stuSchedule) {
                    Intent intent = new Intent(ViewSubjectsActivity.this, StudentDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuInbox) {
                    Intent intent = new Intent(ViewSubjectsActivity.this, InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuProfile) {
                    Intent intent = new Intent(ViewSubjectsActivity.this, StudentProfile.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        subjectView = findViewById(R.id.subjectRecyclerView);
        subjectView.setLayoutManager(new LinearLayoutManager(this));

        subjectList = new ArrayList<>();

        adapter = new SubjectAdapter(subjectList, subject -> {
            Intent intent = new Intent(ViewSubjectsActivity.this, ViewTeachersActivity.class);
            intent.putExtra("subjectID", subject.getSubjectID());
            startActivity(intent);
        });

        subjectView.setAdapter(adapter);

        // Initialize VolleySingleton
        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        subjectList.clear(); // Clear previous data, if any
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonSubject = response.getJSONObject(i);
                                int subjectID = jsonSubject.getInt("subjectID");
                                String subjectName = jsonSubject.getString("subjectName");
                                subjectList.add(new Subject(subjectID, subjectName));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged(); // Notify adapter to refresh the RecyclerView
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}















//
// = new JsonArrayRequest(Request.Method.GET, url, null,
//         new Response.Listener<JSONArray>() {
//@Override
//public void onResponse(JSONArray response) {
//        // Parse the JSON response
//        for (int i = 0; i < response.length(); i++) {
//        Log.e("what is in here", response.toString());
//        try {
//        JSONObject userObject = response.getJSONObject(i);
//        String role = userObject.getString("role");
//        if ("TEACHER".equals(role)) {
//        // Add the user object to the teacherList
//        subjectList.add(userObject);
//        }
//        } catch (JSONException e) {
//        e.printStackTrace();
//        }
//        }
//        // Initialize the adapter with the filtered list of teachers
//        adptr = new UserAdptr(ViewSubjectsActivity.this, subjectList);
//        subjectView.setAdapter(adptr);
//        }
//        },
//        new Response.ErrorListener() {
//@Override
//public void onErrorResponse(VolleyError error) {
//        Log.e("nvm got error", error.toString());
//        error.printStackTrace();
//        }
//        });
//
//        // Add the request to the RequestQueue
//        volleySingleton.addToRequestQueue(jsonArrayRequest);