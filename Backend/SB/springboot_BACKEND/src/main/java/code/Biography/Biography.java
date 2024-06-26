package code.Biography;

import code.ProfileImages.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import code.Users.User;

import javax.persistence.*;

@Entity
public class Biography {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bio;


    private int age;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Biography(String bio, User user, int age, Image image) {
        this.bio = bio;
        this.user = user;
        this.age = age;
        this.image = image;
    }

    public Biography() {
    }

    public Image getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
