package code.Subject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class SubjectRequest{
    private int subjectID;

    private String subjectName;

    public SubjectRequest(String subjectName) {
        this.subjectName = subjectName;
    }

    public SubjectRequest() {
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectID=" + subjectID +
                ", subjectName=" + subjectName +
                '}';
    }
}
