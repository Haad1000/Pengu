package com.example.pengu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeacherBottomSheetTimeSelection extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> timeSlots;
    private String selectedDate;
    private String teacherId;
    private String token;

    private TimeSlotFetcher timeSlotFetcher;

    public static TeacherBottomSheetTimeSelection newInstance(String date, String teacherId, TimeSlotFetcher fetcher, String token) {
        TeacherBottomSheetTimeSelection fragment = new TeacherBottomSheetTimeSelection();
        Bundle args = new Bundle();
        args.putString("selectedDate", date);
        args.putString("teacherId", teacherId);
        args.putString("token", token);
        fragment.setArguments(args);
        fragment.timeSlotFetcher = fetcher;
        Log.d("newInstance", "Tokenmnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn: " + token);// Set the fetcher here


        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDate = getArguments().getString("selectedDate");
            teacherId = getArguments().getString("teacherId");
            token = getArguments().getString("token");

            Log.d("TeacherBottomSheet", "TokenNNNNNNNNNNNNNNNNNNNNNNNNNNN: " + token);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_bottom_sheet_time_selection, container, false);
        recyclerView = view.findViewById(R.id.bottomSheetRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TimeSlotAdapter(new ArrayList<>());  // Initialize with an empty list
        adapter.setListener(new TimeSlotAdapter.TimeSlotClickListener() {
            @Override
            public void onAddButtonClick(TimeSlot timeSlot, int dateId, int timeSlotId) {
                // Handle button click event
            }
        });
        recyclerView.setAdapter(adapter);
        fetchTimeSlotsForSpecificDate();
        return view;
    }

    private void fetchTimeSlotsForSpecificDate() {
        Log.d("fetchTimeSlotsForSpecificDate", "Method called");

        if (selectedDate != null && timeSlotFetcher != null) {
            int dateId = timeSlotFetcher.getDateId(selectedDate);
            if (dateId == -1) {
                Toast.makeText(getContext(), "No valid date ID found for selected date.", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/allByDate/" + teacherId + "/" + dateId;
            Log.d("fetchTimeSlotsForSpecificDate", "URL: " + url);

            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Log.d("fetchTimeSlotsForSpecificDate", "updateRecyclerView about to get called");
                        updateRecyclerView(response);
                    },
                    error -> Log.e("TimeSlotManager", "Error fetching time slots for specific date: " + error.getMessage())
            );
            VolleySingleton.getInstance(getContext()).addToRequestQueue(request);
        }
    }

    private void updateRecyclerView(String response) {
        Log.d("updateRecyclerView", "Method called");

        try {
            JSONArray jsonArray = new JSONArray(response);
            List<TimeSlot> fetchedTimeSlots = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TimeSlot timeSlot = new TimeSlot(
                        jsonObject.getInt("timeSlotId"),
                        jsonObject.getString("startTime"),
                        jsonObject.getString("endTime"),
                        jsonObject.getInt("slotId"),
                        jsonObject.getString("date")
                );
                fetchedTimeSlots.add(timeSlot);
            }
            timeSlots = fetchedTimeSlots; // Update the main list
            Log.d("TeacherBottomSheet", "updateRecyclerView: Time slots fetched successfully, updating adapter");
            adapter.setData(timeSlots);
            adapter.setListener(new TimeSlotAdapter.TimeSlotClickListener() {
                @Override
                public void onAddButtonClick(TimeSlot timeSlot, int dateId, int id) {
                    Log.d("Token", "TokenNNMMNNNNMNNNNMMMMMMMMMMMMMMMMM: " + token);
                    // String token = "your_token_value";
                    // Here you execute the PUT request
                    executePutRequest(dateId, id, timeSlot.getStartTime(), timeSlot.getEndTime(), token);
                }
            });// Update adapter with new data
        } catch (JSONException e) {
            Log.e("TimeSlotManager", "Error parsing time slots", e);
        }
    }

    private void executePutRequest(int dateId, int id , String startTime, String endTime, String token) {
        Log.d("Volley", "executePutRequest method called"); // Add this log message
        String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/update/" + dateId + "/" + id + "/" + token;

        Log.d("Volley", "URL: " + url);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("startTime", startTime);
            jsonBody.put("endTime", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    Log.d("Volley", "Response: " + response);
                    //Toast.makeText(getContext(), "Response: " + response, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Successfully updated time slot.", Toast.LENGTH_SHORT).show();

                },
                error -> {
                    Log.e("Volley", "Error: " + error.toString());
                    Toast.makeText(getContext(), "Failed to update time slot.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");  // Ensure JSON data type
                headers.put("Authorization", "Bearer " + token);  // Add the token as a Bearer Token
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}

