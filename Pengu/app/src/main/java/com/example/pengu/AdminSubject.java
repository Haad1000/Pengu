package com.example.pengu;
public class AdminSubject {
    private int subjectID;
    private String subjectName;

    public AdminSubject(int subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    // Getters and setters
    public int getID() { return subjectID; }
    public void setID(int subjectID) { this.subjectID = subjectID; }
    public String getName() { return subjectName; }
    public void setName(String subjectName) { this.subjectName = subjectName; }
}