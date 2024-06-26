package com.example.pengu;

public class Review {
    private String reviewText;
    private String username;

    private String profname;


    public Review(String username, String reviewText) {
        this.username = username;
        this.reviewText = reviewText;
        this.profname = profname;
    }

    // Getter for reviewText
    public String getReviewText() {
        return reviewText;
    }

    // Setter for reviewText (if needed)
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    public void setUsernameText(String reviewText) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getProfessorName() {
        return profname;
    }
}