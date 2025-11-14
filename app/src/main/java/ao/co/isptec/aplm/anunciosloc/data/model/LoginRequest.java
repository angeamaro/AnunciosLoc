package ao.co.isptec.aplm.anunciosloc.data.model;

public class LoginRequest {
    private String username;
    private String passwordHash;

    public LoginRequest(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
}