package com.example.pengu;
public class BookingInfo {
    private int id;
    private int subjectID;
    private int teacherID;
    private int studentID;
    private String teacherName;
    private String studentName;
    private String date;
    private String startTime;
    private String endTime;

    // Constructor
    public BookingInfo(int id,int subjectID, int teacherID, int studentID, String teacherName, String studentName, String date, String startTime, String endTime) {
        this.id = id;
        this.subjectID = subjectID;
        this.teacherID = teacherID;
        this.studentID = studentID;
        this.teacherName = teacherName;
        this.studentName = studentName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters
    public int getId() { return id; }
    public int getSubjectID() { return subjectID; }
    public int getTeacherID() { return teacherID; }
    public int getStudentID() { return studentID; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    public String getStudentName() { return studentName; }
    public String getTeacherName() { return teacherName; }

//    public void setSubjectName(String newSubjectName) {
//        this.studentName = newSubjectName;
//    }
public void setDate(String date) {
    this.date = date;
}

    // Setter for the start time
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    // Setter for the end time
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }





    // Setters, if needed
    // ...
}
