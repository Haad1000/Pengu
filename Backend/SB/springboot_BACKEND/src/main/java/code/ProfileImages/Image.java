package code.ProfileImages;

import code.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // GenerationType.IDENTITY
    private int id;
    @Column
    private String filePath;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Image() {}

    public Image(String filePath, User user) {
        this.filePath = filePath;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserId(int id) {
        if (this.user == null) {
            this.user = new User();
        }
        this.user.setId(id);
    }


}
