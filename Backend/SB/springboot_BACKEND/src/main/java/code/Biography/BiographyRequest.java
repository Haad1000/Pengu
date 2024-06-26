package code.Biography;

public class BiographyRequest {

    private int id;
    private String bio;
    private int age;
    private int userId; // This will reference the ID of the User entity related to the Biography

    // Constructors
    public BiographyRequest() {
    }

    public BiographyRequest(int id, String bio,int age, int userId) {
        this.id = id;
        this.bio = bio;
        this.age = age;
        this.userId = userId;
    }

    // Getters and Setters
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
