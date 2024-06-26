package com.example.pengu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TimeSlotParser {

    public static List<TimeSlot> parseJSONResponse(String jsonResponse) {
        List<TimeSlot> timeSlots = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String startTime = jsonObject.getString("startTime");
                String endTime = jsonObject.getString("endTime");
                int dateId = jsonObject.getJSONObject("slot").getInt("id");
                String date = jsonObject.getJSONObject("slot").getString("date");

                TimeSlot timeSlot = new TimeSlot(id, startTime, endTime, dateId, date);
                timeSlots.add(timeSlot);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return timeSlots;
    }
}
