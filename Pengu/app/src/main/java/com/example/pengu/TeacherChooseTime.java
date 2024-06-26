package com.example.pengu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;






public class TeacherChooseTime extends AppCompatActivity  {
    private ChoseTimeSlotAdapter adapter;
    private RecyclerView recyclerView;
   // private ChosenTimeSlotAdapter adapter;
    private List<TimeSlot> chosenTimeSlots = new ArrayList<>();
    CalendarView calendar;
    TextView date_view;
    private String selectedDate;
    private Set<String> postedDates = new HashSet<>();
    private TimeSlotFetcher timeSlotManager;
    private BottomNavigationView bottomNavigationView;


    private boolean slotsFetched = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_choose_time);
        recyclerView = findViewById(R.id.chosenslotsRecyclerView);

        bottomNavigationView = findViewById(R.id.teach_navbar);

        bottomNavigationView.setSelectedItemId(R.id.teachSchedule);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.teachDashboard) {
                    Intent intent = new Intent(TeacherChooseTime.this, TeacherDashboard.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachSchedule) {
                    //Currently here
                    return true;
                }
                else if (item.getItemId() == R.id.teachInbox) {
                    Intent intent = new Intent(TeacherChooseTime.this, T_InboxActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId() == R.id.teachProfile) {
                    Intent intent = new Intent(TeacherChooseTime.this, actualTeacherProfile.class);
                    startActivity(intent);
                    return true;
                }
                else {
                    return true;
                }
            }
        });

        recyclerView = findViewById(R.id.chosenslotsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChoseTimeSlotAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        fetchChosenTimeSlots();
        calendar = findViewById(R.id.calendar);
        date_view = findViewById(R.id.date_view);
        timeSlotManager = new TimeSlotFetcher(this);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                try {
//                    SharedPreferences prefs = getSharedPreferences("PostedDates", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.clear();  // Remove all key-value pairs
//                    editor.apply();
                    //Log.d("RemoveAllPostedDates", "All dates removed from SharedPreferences");


                    String formattedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                    String teacherId = retrieveTeacherId();
                    if (teacherId.isEmpty()) {
                        Toast.makeText(TeacherChooseTime.this, "Teacher ID is missing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String token = retrieveToken();
                    if (token.isEmpty()) {
                        Toast.makeText(TeacherChooseTime.this, "Token is missing", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TeacherBottomSheetTimeSelection bottomSheet = TeacherBottomSheetTimeSelection.newInstance(formattedDate, teacherId, timeSlotManager, token);

                    bottomSheet.show(getSupportFragmentManager(), "TAG");
                    selectedDate = formattedDate;
                    displayTimeSlotsForSelectedDate(selectedDate);
                    date_view.setText(formattedDate);
                    postTimeSlotsIfNeeded(formattedDate);
                    fetchAndDisplayTimeSlots();
                } catch (Exception e) {
                    Log.e("DateChangeError", "Error handling date change", e);
                    Toast.makeText(TeacherChooseTime.this, "Error processing date change", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void fetchAndDisplayTimeSlots() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String teacherId = sharedPreferences.getString("userId", "");
        if (teacherId.isEmpty()) {
            Log.e("CreateBooking", "TeacheId is missing.");
            return;
        }
        timeSlotManager.fetchTimeSlots(teacherId);

        slotsFetched = true;


        // After fetching, you might want to update UI to show time slots,
        // depending on how fetchTimeSlots is implemented (sync or async).
    }

    private void displayTimeSlotsForSelectedDate(String date) {
        if (!slotsFetched) {
            fetchAndDisplayTimeSlots();
        } else {
            showTimeSlots(date);
        }
    }



    private void showTimeSlots(String date) {
        List<TimeSlot> slotsForDate = timeSlotManager.getTimeSlotsForDate(date);


        if (!slotsForDate.isEmpty()) {
            for (TimeSlot slot : slotsForDate) {
                Log.d("TimeSlotDetails", "this is SHOWTIMESLOTS");
                Log.d("TimeSlotDetails", "Date: " + date + ", Start: " + slot.getStartTime() + ", End: " + slot.getEndTime());
            }
            //
            // Assuming you have a method to handle showing the BottomSheetDialogFragment
            //showBottomSheetDialogFragment(slotsForDate);
        } else {
            Toast.makeText(this, "No time slots available for this date.", Toast.LENGTH_SHORT).show();
        }
    }
    private void postTimeSlotsIfNeeded(String date) {
        SharedPreferences prefs = getSharedPreferences("PostedDates", MODE_PRIVATE);
        Set<String> postedDates = new HashSet<>(prefs.getStringSet("postedDates", new HashSet<>()));

        if (!postedDates.contains(date)) {
            postTimeSlots(date);
            // Add the date to posted dates and save
            postedDates.add(date);
            showTimeSlots(date);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet("postedDates", postedDates);
            editor.apply();
        } else {
            Log.d("TimeSlotPosting", "Time slots already posted for date: " + date);
        }
    }



    public void postTimeSlots(String selectedDate) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        if (token.isEmpty()) {
            Log.e("CreateBooking", "Auth token is missing.");
            return;
        }
        String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/open/"+token+"/" + selectedDate;
        Log.d("URL", "URL to post time slots: " + url);
        JSONArray slotsArray = new JSONArray();
        try {
            // Define the static time slots
            slotsArray.put(new JSONObject().put("startTime", "08:00").put("endTime", "09:00"));
            slotsArray.put(new JSONObject().put("startTime", "10:00").put("endTime", "11:00"));
            slotsArray.put(new JSONObject().put("startTime", "12:00").put("endTime", "13:00"));
            Log.d("JSON to Send", slotsArray.toString()); // Log the JSON to verify its structure
        } catch (JSONException e) {
            Log.e("JSON Exception", "Error creating JSON for slots", e);
            Toast.makeText(TeacherChooseTime.this, "Failed to create data for slots.", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("POST Slots Success", "Successfully posted slots: " + response);
                    Toast.makeText(this, "Time slots posted successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("Volley Error", "Error posting slots: " + error.getMessage());
                    Toast.makeText(this, "Failed to post slots: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return slotsArray.toString().getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                if (!token.isEmpty()) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

//    public static TeacherBottomSheetTimeSelection newInstance(List<TimeSlot> timeSlots) {
//        TeacherBottomSheetTimeSelection fragment = new TeacherBottomSheetTimeSelection();
//        Bundle args = new Bundle();
//        args.putSerializable("timeSlots", new ArrayList<>(timeSlots));  // Your TimeSlot class needs to implement Serializable
//        fragment.setArguments(args);
//        return fragment;
//    }

    private String retrieveTeacherId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        // Fetch and return the teacher ID using the correct key. Assuming 'userId' is the key for teacher ID.
        return sharedPreferences.getString("userId", ""); // Return an empty string if no ID is found.
    }

    private String retrieveToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        String token = sharedPreferences.getString("AuthToken", "");
        Log.d("Token", "Retrieved token: " + token); // Log out the retrieved token
        return token;
    }
    private void fetchChosenTimeSlots() {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/available/"+retrieveTeacherId(); // Adjust URL as necessary

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        List<ChoseTimeSlot> slots = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject slotObject = jsonArray.getJSONObject(i);
                            ChoseTimeSlot slot = new ChoseTimeSlot(
                                    slotObject.getString("date"),
                                    slotObject.getInt("timeSlotId"),
                                    slotObject.getInt("slotId"),
                                    slotObject.getString("startTime"),
                                    slotObject.getString("endTime")
                            );
                            slots.add(slot);
                        }
                        adapter.updateSlots(slots); // Make sure your adapter has this method
                    } catch (JSONException e) {
                        Log.e("JSONError", "Failed to parse JSON", e);
                    }
                },
                error -> Log.e("Volley", "Error with request: " + error.getMessage())
        );

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }




}


    // Other code...




