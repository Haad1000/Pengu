package com.example.pengu;

public class Subject {
    private int subjectID;
    private String subjectName;

    public Subject(int subjectID, String subjectName) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
