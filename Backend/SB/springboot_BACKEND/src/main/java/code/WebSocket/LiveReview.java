package code.WebSocket;

import code.Users.User;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "LiveReviews")
public class LiveReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user_id", nullable = false)
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user_id", nullable = false)
    private User to;

    @Lob
    private String review;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    public LiveReview() {}

    public LiveReview(User from, User to, String review) {
        this.from = from;
        this.to = to;
        this.review = review;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getContent() {
        return review;
    }

    public void setContent(String content) {
        this.review = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}