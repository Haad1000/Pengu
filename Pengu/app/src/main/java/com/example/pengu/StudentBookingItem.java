package com.example.pengu;

public class StudentBookingItem {
    private String subjectName;
    private String teacherName;
    private String date;
    private String startTime;

    public StudentBookingItem(String subjectName, String studentName, String date, String startTime) {
        this.subjectName = subjectName;
        this.teacherName = studentName;
        this.date = date;
        this.startTime = startTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }
}
