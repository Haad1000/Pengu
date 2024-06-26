package code.Reports;

import code.Users.User;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ReportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private User studentReporting;

    private int teacherReportedID;

    private String title;

    private String description;

    public ReportRequest(){}

    public ReportRequest(User studentReporting, int teacherReported, String title, String description) {
        this.studentReporting = studentReporting;
        this.teacherReportedID = teacherReported;
        this.title = title;
        this.description = description;
    }

    public User getStudentReporting() {
        return studentReporting;
    }

    public void setStudentReporting(User studentReporting) {
        this.studentReporting = studentReporting;
    }

    public int getTeacherReportedID() {
        return teacherReportedID;
    }

    public void setTeacherReportedID(int teacherReportedID) {
        this.teacherReportedID = teacherReportedID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
