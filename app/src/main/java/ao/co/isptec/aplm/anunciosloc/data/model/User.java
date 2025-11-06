package ao.co.isptec.aplm.anunciosloc.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Modelo de Usuário
 */
public class User {
    private String id;
    private String username;
    private String email;
    private String password; // Apenas para uso local, não será enviado ao servidor
    private String name;
    private String phoneNumber;
    private String photoUrl;
    private String publicKey; // Chave pública do usuário para criptografia
    private Map<String, String> profileAttributes; // Pares chave-valor (ex: "clube"="Benfica")
    private long createdAt;
    private long lastLoginAt;
    
    public User() {
        this.profileAttributes = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
    }
    
    public User(String id, String username, String email, String name) {
        this();
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    
    public Map<String, String> getProfileAttributes() {
        return profileAttributes;
    }
    
    public void setProfileAttributes(Map<String, String> profileAttributes) {
        this.profileAttributes = profileAttributes;
    }
    
    /**
     * Adiciona um atributo ao perfil
     */
    public void addProfileAttribute(String key, String value) {
        this.profileAttributes.put(key, value);
    }
    
    /**
     * Remove um atributo do perfil
     */
    public void removeProfileAttribute(String key) {
        this.profileAttributes.remove(key);
    }
    
    /**
     * Obtém um atributo do perfil
     */
    public String getProfileAttribute(String key) {
        return this.profileAttributes.get(key);
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
