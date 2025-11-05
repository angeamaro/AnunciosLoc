package ao.co.isptec.aplm.anunciosloc.data.model;

/**
 * Regra de política de entrega
 * Ex: attributeKey="profissao", attributeValue="Estudante"
 */
public class PolicyRule {
    private String attributeKey;   // Ex: "profissao", "clube", "interesse"
    private String attributeValue; // Ex: "Estudante", "Benfica", "Tecnologia"
    
    public PolicyRule() {
    }
    
    public PolicyRule(String attributeKey, String attributeValue) {
        this.attributeKey = attributeKey;
        this.attributeValue = attributeValue;
    }
    
    public String getAttributeKey() {
        return attributeKey;
    }
    
    public void setAttributeKey(String attributeKey) {
        this.attributeKey = attributeKey;
    }
    
    public String getAttributeValue() {
        return attributeValue;
    }
    
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
    
    @Override
    public String toString() {
        return attributeKey + " = " + attributeValue;
    }
}
