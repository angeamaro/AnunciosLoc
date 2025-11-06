package ao.co.isptec.aplm.anunciosloc.data.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa um filtro de política baseado em atributos de perfil
 * Exemplo: {"interesse": "Tecnologia", "profissao": "Estudante"}
 */
public class PolicyFilter implements Serializable {
    private Map<String, String> attributes; // Pares chave-valor
    private String type; // "whitelist" ou "blacklist"
    
    public PolicyFilter() {
        this.attributes = new HashMap<>();
    }
    
    public PolicyFilter(String type) {
        this();
        this.type = type;
    }
    
    public Map<String, String> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }
    
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    public void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }
    
    public void removeAttribute(String key) {
        if (attributes != null) {
            attributes.remove(key);
        }
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isEmpty() {
        return attributes == null || attributes.isEmpty();
    }
    
    /**
     * Verifica se um usuário corresponde ao filtro
     */
    public boolean matches(User user) {
        if (isEmpty() || user == null) {
            return false;
        }
        
        Map<String, String> userProfile = user.getProfileAttributes();
        if (userProfile == null || userProfile.isEmpty()) {
            return false;
        }
        
        // Verifica se todos os atributos do filtro correspondem aos do usuário
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String key = entry.getKey();
            String requiredValue = entry.getValue();
            String userValue = userProfile.get(key);
            
            if (userValue == null || !userValue.equalsIgnoreCase(requiredValue)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (attributes != null && !attributes.isEmpty()) {
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return sb.toString();
    }
}
