package code.TeacherCourses;

import code.Subject.Subject;
import code.Users.Roles;

import java.util.ArrayList;
import java.util.List;

public class TeacherRequest {

    private int id;
    private String name;
    private String emailId;
    private String password;
    private Roles.UserRoles role;
    private List<Subject> teacherCourses = new ArrayList<>();

    // Added subjectRequest member variable
    private SubjectRequest subjectRequest;

    // Inner class SubjectRequest
    public static class SubjectRequest {
        private int subjectID;
        private String subjectName;

        public SubjectRequest(String subjectName) {
            this.subjectName = subjectName;
        }

        public SubjectRequest() {
        }

        // Getters and setters for SubjectRequest
        public int getSubjectID() {
            return subjectID;
        }

        public void setSubjectID(int subjectID) {
            this.subjectID = subjectID;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        @Override
        public String toString() {
            return "Subject{" +
                    "subjectID=" + subjectID +
                    ", subjectName='" + subjectName + '\'' +
                    '}';
        }
    }

    // Getters and setters for TeacherRequest
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles.UserRoles getRole() {
        return role;
    }

    public void setRole(Roles.UserRoles role) {
        this.role = role;
    }

    public List<Subject> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(List<Subject> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }

    // Getters and setters for subjectRequest
    public SubjectRequest getSubjectRequest() {
        return subjectRequest;
    }

    public void setSubjectRequest(SubjectRequest subjectRequest) {
        this.subjectRequest = subjectRequest;
    }

    @Override
    public String toString() {
        return "TeacherRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", teacherCourses=" + teacherCourses +
                ", subjectRequest=" + subjectRequest +
                '}';
    }
}
