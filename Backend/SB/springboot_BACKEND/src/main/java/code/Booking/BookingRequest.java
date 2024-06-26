package code.Booking;

import code.Subject.Subject;
import code.Users.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.awt.image.LookupOp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;


public class BookingRequest {
    private int ID;

    private Subject subject;

    private User teacher;

    private User student;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;


    public BookingRequest(String subjectName, User teacher, User student, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.subject = subject;
        this.teacher = teacher;
        this.student = student;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BookingRequest() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }



    @Override
    public String toString() {
        return "Booking{" +
                "ID=" + ID +
                ", subject=" + subject +
                ", teacher=" + teacher +
                ", student=" + student +
                ", day='" + date + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
