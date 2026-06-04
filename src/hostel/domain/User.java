package hostel.domain;

public class User {
    private String userId;
    private String username;
    private String role;
    
    public User() {}
    
    public User(String username, String role) {
        this.username = username;
        this.role = role;
        this.userId = username;
    }
    
    // Getters
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRole() {
        return role;
    }
    
    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}