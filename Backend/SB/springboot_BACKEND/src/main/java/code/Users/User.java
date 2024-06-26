package code.Users;

import code.Achievements.Achievement;
import code.Achievements.AchievementRepository;
import org.hamcrest.core.Is;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public class User {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String emailId;
    private String Password;
    private Roles.UserRoles role;
    private IsActive isActive;
    private int numberOfBookings;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_achievements", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "achievements")
    private List<Integer> achievements;

    public User(String name, String emailId, String Password, Roles.UserRoles role, IsActive isActive) {
        this.name = name;
        this.emailId = emailId;
        this.Password = Password;
        this.role = role;
        this.isActive = isActive;
    }

    public User() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmailId(){
        return emailId;
    }

    public void setEmailId(String emailId){
        this.emailId = emailId;
    }

    public String getPassword(){
        return Password;
    }

    public void setPassword(String emailId){
        this.Password = emailId;
    }

    public Roles.UserRoles getRole() {
        return role;
    }

    public void setRole(Roles.UserRoles role) {
        this.role = role;
    }

    public void setImagePath(String filePath) {
    }

    public IsActive getIsActive() {
        return isActive;
    }

    public void setIsActive(IsActive isActive) {
        this.isActive = isActive;
    }

    public List<Integer> getAchievements() {
        return achievements;
    }

    public void addAchievement(int num) {
        achievements.add(num);
    }

    public int getNumberOfBookings() {
        return numberOfBookings;
    }

    public void setNumberOfBookings(int numberOfBookings) {
        this.numberOfBookings = numberOfBookings;
    }
}
