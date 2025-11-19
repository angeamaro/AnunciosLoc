package ao.co.isptec.aplm.anunciosloc.data.model;

public class LoginResponse {
    private String message;
    private String token;
    private Long userId;
    private String username;
    private String expiresAt;

    // Getters
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getExpiresAt() { return expiresAt; }
}
