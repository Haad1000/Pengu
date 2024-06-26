package code.Review_FINAL;

import code.Users.User;

import javax.persistence.*;


public class ReviewRequest {

    private int ID;

    private User writtenBy;

    private User writtenFor;

    private String actualReview;

    public ReviewRequest(User writtenBy, User writtenFor, String actualReview) {
        this.writtenBy = writtenBy;
        this.writtenFor = writtenFor;
        this.actualReview = actualReview;
    }

    public ReviewRequest() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public User getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(User writtenBy) {
        this.writtenBy = writtenBy;
    }

    public User getWrittenFor() {
        return writtenFor;
    }

    public void setWrittenFor(User writtenFor) {
        this.writtenFor = writtenFor;
    }

    public String getActualReview() {
        return actualReview;
    }

    public void setActualReview(String actualReview) {
        this.actualReview = actualReview;
    }



}
