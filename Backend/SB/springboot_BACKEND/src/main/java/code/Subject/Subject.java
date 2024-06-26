package code.Subject;

import code.TeacherCourses.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subjectID;

    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "teacher_id") // adjust the column name as needed
    @JsonIgnore
    private Teacher teacher;

    public Subject(String subjectName) {
        this.subjectName = subjectName;
    }

    public Subject() {
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public String  getSubjectName() {
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
