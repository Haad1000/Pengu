package code.Rating;

import code.Users.User;

import javax.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User student;

    @ManyToOne
    private User teacher;

    private int overall_rating;

    private int communication_rating;

    private int teaching_rating;

    private int recommend_rating;

    public Rating(User student, User teacher, int communication_rating, int teaching_rating, int recommend_rating) {
        this.student = student;
        this.teacher = teacher;
        this.overall_rating = (communication_rating + teaching_rating + recommend_rating) / 3;
        this.communication_rating = communication_rating;
        this.teaching_rating = teaching_rating;
        this.recommend_rating = recommend_rating;
    }

    public Rating(){}

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
