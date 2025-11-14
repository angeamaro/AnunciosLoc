package ao.co.isptec.aplm.anunciosloc.data.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Modelo de Domínio ÚNICO para o User, alinhado com o back-end.
 */
public class User {
    private Long id;
    private String username;
    private String password; // Simplificado para o front-end, o back-end pode usar passwordHash
    private LocalDateTime createdAt;
    private Map<String, String> profileAttributes;

    // Construtor para facilitar a criação
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.profileAttributes = new HashMap<>();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Map<String, String> getProfileAttributes() { return profileAttributes; }
    public void setProfileAttributes(Map<String, String> profileAttributes) { this.profileAttributes = profileAttributes; }
}
