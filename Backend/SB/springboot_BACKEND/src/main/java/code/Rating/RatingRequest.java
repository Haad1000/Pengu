package code.Rating;

import code.Users.User;

import javax.persistence.ManyToOne;

import code.Users.User;

import javax.persistence.ManyToOne;

public class RatingRequest {

    private User student;

    private int teacherId;

    private int overall_rating;

    private int communication_rating;

    private int teaching_rating;

    private int recommend_rating;

    public RatingRequest(int teacherId, int teaching_rating, int recommend_rating, int communication_rating) {
        this.teacherId = teacherId;
        this.communication_rating = communication_rating;
        this.teaching_rating = teaching_rating;
        this.recommend_rating = recommend_rating;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(int overall_rating) {
        this.overall_rating = overall_rating;
    }

    public int getCommunication_rating() {
        return communication_rating;
    }

    public void setCommunication_rating(int communication_rating) {
        this.communication_rating = communication_rating;
    }

    public int getTeaching_rating() {
        return teaching_rating;
    }

    public void setTeaching_rating(int teaching_rating) {
        this.teaching_rating = teaching_rating;
    }

    public int getRecommend_rating() {
        return recommend_rating;
    }

    public void setRecommend_rating(int recommend_rating) {
        this.recommend_rating = recommend_rating;
    }
}

