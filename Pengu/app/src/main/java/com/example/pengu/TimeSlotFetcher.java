package com.example.pengu;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TimeSlotFetcher {
    private Context context;
    private Map<String, List<TimeSlot>> timeSlotsMap = new HashMap<>();
    private Map<String, Integer> dateToIdMap = new HashMap<>();

    public TimeSlotFetcher(Context context) {
        this.context = context;
    }
    public void fetchTimeSlots(String teacherId) {
        String url = "http://coms-309-037.class.las.iastate.edu:8080/slots/all/" + teacherId;
        Log.d("TimeSlotManager", "Fetching time slots from URL: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("TimeSlotFetcher", "Time slots fetched successfully.");
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TimeSlot timeSlot = new TimeSlot(
                                    jsonObject.getInt("timeSlotId"),
                                    jsonObject.getString("startTime"),
                                    jsonObject.getString("endTime"),
                                    jsonObject.getInt("slotId"),
                                    jsonObject.getString("date")
                            );

                            String date = jsonObject.getString("date");
                            int dateId = jsonObject.getInt("slotId");

                            if (jsonObject.has("slotId")) {

                                dateToIdMap.put(date, dateId);
                            }

                            if (!timeSlotsMap.containsKey(date)) {
                                timeSlotsMap.put(date, new ArrayList<>());

                            }
                            timeSlotsMap.get(date).add(timeSlot);
                        }
                        for (Map.Entry<String, List<TimeSlot>> entry : timeSlotsMap.entrySet()) {
                            String date = entry.getKey();
                            List<TimeSlot> timeSlots = entry.getValue();
                            Log.d("TimeSlotFetcher", "Date: " + date);
                            for (TimeSlot slot : timeSlots) {
                                Log.d("TimeSlotFetcher", "TimeSlot: " + slot);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("TimeSlotManager", "Error parsing time slots", e);
                    }
                },
                error -> Log.e("TimeSlotManager", "Error fetching time slots: " + error.getMessage())
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    public List<TimeSlot> getTimeSlotsForDate(String date) {
        return timeSlotsMap.getOrDefault(date, new ArrayList<>());
    }

    public int getDateId(String date) {
        int dateId = dateToIdMap.getOrDefault(date, -1); // Returns -1 if the date is not found
        Log.d("TimeSlotFetcher", "Date: " + date + ", Date ID: " + dateId); // Log the date and its corresponding ID
        return dateId;
    }

}
