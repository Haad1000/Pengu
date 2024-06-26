package com.example.pengu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class ViewTeachersActivity extends AppCompatActivity {

    private RecyclerView teacherRecyclerView;
    private TeacherAdapter adapter;
    private List<Teacher> teacherList;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String url = "http://coms-309-037.class.las.iastate.edu:8080/getTeachersBySubject/";
    private String GetUrl;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_teachers);

        bottomNavigationView = findViewById(R.id.student_navbar);

        bottomNavigationView.setSelectedItemId(R.id.stuDashboard);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.stuDashboard) {
                    Intent intent = new Intent(ViewTeachersActivity.this, ViewSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuSchedule) {
                    Intent intent = new Intent(ViewTeachersActivity.this, StudentDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuInbox) {
                    Intent intent = new Intent(ViewTeachersActivity.this, InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuProfile) {
                    Intent intent = new Intent(ViewTeachersActivity.this, StudentProfile.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        teacherRecyclerView = findViewById(R.id.teacherRecyclerView);
        teacherRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int subjectID = getIntent().getIntExtra("subjectID", -1);

        teacherList = new ArrayList<>();
        adapter = new TeacherAdapter(teacherList, teacher -> {
            // Handle item click, e.g., navigate to another activity with teacher id
            Intent intent = new Intent(ViewTeachersActivity.this, TeacherProfile.class);
            intent.putExtra("teacherID", teacher.getId());
            intent.putExtra("subjectID", subjectID);
            startActivity(intent);
        });

        teacherRecyclerView.setAdapter(adapter);

        volleySingleton = VolleySingleton.getInstance(this);
        requestQueue = volleySingleton.getRequestQueue();

        if (subjectID == -1) {
            Log.e("ViewTeachersActivity", "SubjectID not found");
            return;
        }

        GetUrl = url + subjectID;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                GetUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        teacherList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonTeacher = response.getJSONObject(i);
                                int id = jsonTeacher.getInt("id");
                                String name = jsonTeacher.getString("name");

                                teacherList.add(new Teacher(id, name));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged(); // Refresh RecyclerView
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
