package com.example.pengu;

public class AllUser {
    private String name;
    private String email;
    private String role;
    private boolean isBanned;

    public AllUser(String name, String email, String role,boolean isBanned) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.isBanned = isBanned;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
    public boolean isBanned() {
        return isBanned;
    }
}
