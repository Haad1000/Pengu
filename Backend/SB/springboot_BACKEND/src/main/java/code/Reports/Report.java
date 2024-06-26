package code.Reports;

import code.Users.User;

import javax.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    private User studentReporting;

    @OneToOne
    private User teacherReported;

    private String title;

    private String description;

    private ReportStatus status;

    public Report(){}

    public Report(User studentReporting, User teacherReported, String title, String description, ReportStatus status) {
        this.studentReporting = studentReporting;
        this.teacherReported = teacherReported;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public User getStudentReporting() {
        return studentReporting;
    }

    public void setStudentReporting(User studentReporting) {
        this.studentReporting = studentReporting;
    }

    public User getTeacherReported() {
        return teacherReported;
    }

    public void setTeacherReported(User teacherReported) {
        this.teacherReported = teacherReported;
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

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
