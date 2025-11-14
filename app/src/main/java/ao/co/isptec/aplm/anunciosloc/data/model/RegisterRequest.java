package ao.co.isptec.aplm.anunciosloc.data.model;

public class RegisterRequest {
    private String username;
    private String passwordHash;

    public RegisterRequest(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}