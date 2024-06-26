package com.example.pengu;

public class TimeSlot {
    private int id;
    private String startTime;
    private String endTime;
    private int dateId;

    private String date;
    // You can add more fields as needed

    public TimeSlot(int id, String startTime, String endTime, int dateId, String date) {
        this.id = id;//timeslotid
        this.startTime = startTime;
        this.endTime = endTime;
        this.dateId = dateId;//slotId
        this.date = date;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getDateId() {
        return dateId;
    }

    public String getDate(){
        return date;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }
    public void setDate(String date) {
        this.date= date;
    }


    // toString method to print the TimeSlot object
    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", dateId='" + dateId + '\'' +
                ", date='" + date + '\'' +

                '}';
    }
}
