package ao.co.isptec.aplm.anunciosloc.data.model;

public class LoginResponse {
    private String sessionToken;
    private User user;

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}