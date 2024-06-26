package com.example.pengu;

public class Subjects {
    private int subjectID;
    private String subjectName;

    // Constructor, getters, and setters
    public Subjects(int subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    public int getSubjectID() { return subjectID; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectID(int subjectID) { this.subjectID = subjectID; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
}
