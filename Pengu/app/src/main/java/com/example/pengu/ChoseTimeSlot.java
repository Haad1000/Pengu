package com.example.pengu;

public class ChoseTimeSlot {
    private String date;
    private int timeSlotId;
    private int slotId;
    private String startTime;
    private String endTime;

    // Constructor, getters, and setters
    public ChoseTimeSlot(String date, int timeSlotId, int slotId, String startTime, String endTime) {
        this.date = date;
        this.timeSlotId = timeSlotId;
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public String getDate() { return date; }
    public int getTimeSlotId() { return timeSlotId; }
    public int getSlotId() { return slotId; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}