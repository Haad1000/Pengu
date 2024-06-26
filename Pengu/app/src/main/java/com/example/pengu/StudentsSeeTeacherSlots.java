package com.example.pengu;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class StudentsSeeTeacherSlots extends AppCompatActivity {
    private StudentChoseTimeSlotAdapter adapter;

    private RecyclerView recyclerView;
    // private ChosenTimeSlotAdapter adapter;
//    private List<TimeSlot> chosenTimeSlots = new ArrayList<>();
//    CalendarView calendar;
//    TextView date_view;
//    private String selectedDate;
//    private Set<String> postedDates = new HashSet<>();
    private TimeSlotFetcher timeSlotManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_see_teacher_slots);
        recyclerView = findViewById(R.id.chosenslotsRecyclerView);

        recyclerView = findViewById(R.id.chosenslotsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StudentChoseTimeSlotAdapter.OnSlotClickListener listener = slot -> {
            postData(slot.getDate(), slot.getStartTime(), slot.getEndTime());
        };
        adapter = new StudentChoseTimeSlotAdapter(new ArrayList<>(),listener);
        recyclerView.setAdapter(adapter);
        fetchChosenTimeSlots();

        timeSlotManager = new TimeSlotFetcher(this);

    }


    private void fetchChosenTimeSlots() {
        int teacherID = getIntent().getIntExtra("SubjectID",-1);
        if(teacherID== -1){
            Log.e("StudentSeeTeacherSlots","TeacherId is not found");
        }
//        String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/available/"+teacherID;
        String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/available/"+1;// Adjust URL as necessary

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
    private void postData(String date, String startTime, String endTime) {
        // Implement your POST request logic here
    }

}