package code.TeacherCourses;

import code.Subject.Subject;
import code.Users.IsActive;
import code.Users.Roles;
import code.Users.User;
import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Teacher")
public class Teacher extends User {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "teacher")
    private List<Subject> teacherCourses = new ArrayList<>();

    // Constructors
    public Teacher() {
        super(); // Call User's constructor
    }

    public Teacher(String name, String emailId, String password, Roles.UserRoles role, IsActive isActive, List<Subject> teacherCourses) {
        super(name, emailId, password, role, isActive); // Pass these to the User's constructor
        this.teacherCourses = teacherCourses;
    }

    // Getter and setter for the teacherCourses field
    public List<Subject> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(List<Subject> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }

    // There is no need to override toString, equals, or hashCode methods unless you need specific behavior for Teacher
}
