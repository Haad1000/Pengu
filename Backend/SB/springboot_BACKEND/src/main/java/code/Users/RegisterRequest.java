package code.Users;

public class RegisterRequest {
    private String name;
    private String emailId;
    private String password;
    private Roles.UserRoles role;
    private IsActive isActive;

    // Constructors, getters, and setters
    public RegisterRequest() {}

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

    public IsActive getIsActive() {
        return isActive;
    }

    public void setIsActive(IsActive isActive) {
        this.isActive = isActive;
    }
}

