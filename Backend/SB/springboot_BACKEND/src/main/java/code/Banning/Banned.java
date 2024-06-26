package code.Banning;

import code.Users.Roles;
import code.Users.User;

import javax.persistence.*;

@Entity
public class Banned {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String emailId;
    private String Password;
    private Roles.UserRoles role;


    public Banned(String name, String emailId, String Password, Roles.UserRoles role) {
        this.name = name;
        this.emailId = emailId;
        this.Password = Password;
        this.role = role;
    }

    public Banned() {
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
}
