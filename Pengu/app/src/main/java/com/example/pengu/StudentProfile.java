package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {
    private String BASE_BIO_URL = "http://coms-309-037.class.las.iastate.edu:8080/getBio/";
    private String BASE_USER_URL = "http://coms-309-037.class.las.iastate.edu:8080/getUser/";
//    private String BASE_PROFILE_URL = "http://coms-309-037.class.las.iastate.edu:8080/images/";
    private String BASE_PROFILE_URL = "http://coms-309-037.class.las.iastate.edu:8080/images/user/";
    private String GET_ALL_ACH = "http://coms-309-037.class.las.iastate.edu:8080/getAllAch";
    private String GET_ACT_ACH = "http://coms-309-037.class.las.iastate.edu:8080/getUserByID/";
    private String Bio_Url, User_Url, Pfp_Url, Active_Ach, User_Id, authToken;
    private TextView profileName, profileBio;
    private ImageButton toEditProfile;
    private BottomNavigationView bottomNavigationView;
    private CircleImageView pfp;
    private List<Achievement> allAchievements = new ArrayList<>();
    private List<Integer> userAchievements = new ArrayList<>();
    private RecyclerView allAchView;
    private AchAdapter achadptr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        authToken = sharedPreferences.getString("AuthToken", "");
        User_Id = sharedPreferences.getString("userId","");
        Log.d("The user Token is: ", authToken);
        Log.d("The user is is: ", User_Id);

        bottomNavigationView = findViewById(R.id.student_navbar);

        bottomNavigationView.setSelectedItemId(R.id.stuProfile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.stuDashboard) {
                    Intent intent = new Intent(StudentProfile.this, ViewSubjectsActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuSchedule) {
                    Intent intent = new Intent(StudentProfile.this, StudentDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuInbox) {
                    Intent intent = new Intent(StudentProfile.this, InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.stuProfile) {
                    //currently here
                    return true;
                }
                else {
                    return true;
                }
            }
        });
        
        profileName = findViewById(R.id.name);
        profileBio = findViewById(R.id.aboutMeBox);
        pfp = findViewById(R.id.stupfp);
        toEditProfile = findViewById(R.id.optionButton);
        allAchView = findViewById(R.id.allAchRecycleView);
        achadptr = new AchAdapter(this, allAchievements);
        allAchView.setAdapter(achadptr);

        allAchView.setLayoutManager(new LinearLayoutManager(this));

        Bio_Url = BASE_BIO_URL + User_Id;
        User_Url = BASE_USER_URL + authToken;
        Pfp_Url = BASE_PROFILE_URL + User_Id;
        Active_Ach = GET_ACT_ACH + User_Id;

        fetchActiveAchievements(Active_Ach);

        fetchUserInfo(User_Url);
        fetch_Bio(Bio_Url);
        fetchProfilePic(Pfp_Url);
        populateAllAch(GET_ALL_ACH);

        toEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentProfile.this, StudentProfileEdit.class);
                startActivity(intent);
            }
        });
    }

    private void fetchActiveAchievements(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray achievementsArray = response.getJSONArray("achievements");
                            userAchievements.clear();  // Clear previous data
                            for (int i = 0; i < achievementsArray.length(); i++) {
                                userAchievements.add(achievementsArray.getInt(i));
                            }

                            // Update the adapter with the list of active achievements
                            achadptr.updateUserAchievements(userAchievements);  // Custom method
                        } catch (JSONException e) {
                            Log.e("Achievement Error", "Error parsing active achievements: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error fetching active achievements: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);  // Add the request to start fetching
    }

    private void populateAllAch(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Clear the list before adding new data
                allAchievements.clear();  // This avoids duplicating data if called multiple times
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject achObject = response.getJSONObject(i);
                        int id = achObject.getInt("id");
                        String title = achObject.getString("title");
                        String desc = achObject.getString("desc");

                        // Create a new Achievement object and add it to the list
                        Achievement achievement = new Achievement(id, title, desc);
                        allAchievements.add(achievement);
                    }

                    // Initialize and set the adapter with the updated list
                    achadptr = new AchAdapter(StudentProfile.this, allAchievements);
                    allAchView.setAdapter(achadptr);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Error", "Error parsing achievements: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Error fetching achievements: " + error.getMessage());
            }
        });

        requestQueue.add(jsonArrayRequest);  // Add the request to the queue to start fetching
    }


    private void fetch_Bio(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Bio Response",response.toString());
                    profileBio.setText(response);
                }
                catch(Exception e) {
                    Log.e("Bio here", "Error parsing user info: " + e.getMessage());
                }
            }
        },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Another Bio Here", "Error fetching user info: " + error.getMessage());
                }
            }
        );

        requestQueue.add(stringRequest);
    }

    private void fetchUserInfo(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "name" field from the JSON object and set it to profileName
                            String name = response.getString("name");
                            String userId = response.getString("id");
                            User_Id = userId;
                            profileName.setText(name);
                            Log.e("User Response", response.toString());
                        } catch (JSONException e) {
                            Log.e("Name here", "Error parsing user info: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Another Name Here", "Error fetching user info: " + error.getMessage());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void fetchProfilePic(String url) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        pfp.setImageBitmap(response);
                    }
                },
                0, // Width, set to 0 to get the original width
                0, // Height, set to 0 to get the original height
                ImageView.ScaleType.FIT_XY, // ScaleType
                Bitmap.Config.RGB_565, // Bitmap config

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors here
                        Log.e("Volley Error", error.toString());
                    }
                }
        );
        requestQueue.add(imageRequest);
    }
}