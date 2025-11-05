package ao.co.isptec.aplm.anunciosloc.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Modelo de Anúncio completo com políticas e janela temporal
 */
public class Announcement {
    private String id;
    private String title;
    private String content;
    private String locationId; // ID da localização onde foi publicado
    private String locationName; // Nome da localização (para exibição)
    private String authorId; // ID do usuário que criou
    private String authorName; // Nome do autor (para exibição)
    
    // Janela temporal
    private Date startDate; // Data de início da validade
    private Date endDate; // Data de fim da validade
    
    // Política de entrega
    private String deliveryPolicy; // WHITELIST, BLACKLIST ou EVERYONE
    private List<PolicyRule> policyRules; // Regras de filtragem
    
    // Metadados
    private long createdAt;
    private long updatedAt;
    private String status; // ACTIVE, EXPIRED, DRAFT
    private int viewCount; // Número de visualizações
    private boolean isLocal; // Se foi recebido localmente ou do servidor
    
    public Announcement() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.status = Constants.STATUS_ACTIVE;
        this.policyRules = new ArrayList<>();
        this.deliveryPolicy = Constants.POLICY_EVERYONE;
        this.viewCount = 0;
        this.isLocal = false;
    }
    
    public Announcement(String id, String title, String content, String locationId, String authorId) {
        this();
        this.id = id;
        this.title = title;
        this.content = content;
        this.locationId = locationId;
        this.authorId = authorId;
    }
    
    // Getters e Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getLocationId() {
        return locationId;
    }
    
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public String getDeliveryPolicy() {
        return deliveryPolicy;
    }
    
    public void setDeliveryPolicy(String deliveryPolicy) {
        this.deliveryPolicy = deliveryPolicy;
    }
    
    public List<PolicyRule> getPolicyRules() {
        return policyRules;
    }
    
    public void setPolicyRules(List<PolicyRule> policyRules) {
        this.policyRules = policyRules;
    }
    
    public void addPolicyRule(PolicyRule rule) {
        this.policyRules.add(rule);
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
    
    public long getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public boolean isLocal() {
        return isLocal;
    }
    
    public void setLocal(boolean local) {
        isLocal = local;
    }
    
    /**
     * Verifica se o anúncio está ativo (dentro da janela temporal e não expirado)
     */
    public boolean isActive() {
        long now = System.currentTimeMillis();
        
        if (startDate != null && startDate.getTime() > now) {
            return false; // Ainda não começou
        }
        
        if (endDate != null && endDate.getTime() < now) {
            return false; // Já expirou
        }
        
        return Constants.STATUS_ACTIVE.equals(status);
    }
    
    /**
     * Verifica se um usuário pode receber este anúncio baseado nas políticas
     */
    public boolean canUserReceive(Map<String, String> userAttributes) {
        if (Constants.POLICY_EVERYONE.equals(deliveryPolicy)) {
            return true;
        }
        
        if (policyRules == null || policyRules.isEmpty()) {
            return Constants.POLICY_EVERYONE.equals(deliveryPolicy);
        }
        
        boolean matchesRule = false;
        for (PolicyRule rule : policyRules) {
            String userValue = userAttributes.get(rule.getAttributeKey());
            if (userValue != null && userValue.equalsIgnoreCase(rule.getAttributeValue())) {
                matchesRule = true;
                break;
            }
        }
        
        // WHITELIST: usuário precisa corresponder a pelo menos uma regra
        // BLACKLIST: usuário NÃO pode corresponder a nenhuma regra
        if (Constants.POLICY_WHITELIST.equals(deliveryPolicy)) {
            return matchesRule;
        } else if (Constants.POLICY_BLACKLIST.equals(deliveryPolicy)) {
            return !matchesRule;
        }
        
        return true;
    }
}
