package ao.co.isptec.aplm.anunciosloc.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Modelo para anúncios recebidos via P2P (WiFi Direct)
 */
public class P2PAnnouncement implements Serializable {
    private String id;
    private String title;
    private String description;
    private String locationName;
    private double latitude;
    private double longitude;
    private String senderUsername;
    private String senderDeviceId;
    private Date receivedAt;
    private Date windowStart;
    private Date windowEnd;
    private boolean isAuthentic; // Verificado com assinatura
    private ReceivedType receivedType; // DIRECT ou VIA_MULE
    private int retransmissionCount; // Quantas vezes foi retransmitido
    private boolean isPendingRetransmission; // Se é para retransmitir (mula)
    private String targetDeviceId; // ID do dispositivo destino (se for mula)

    public enum ReceivedType {
        DIRECT,      // Recebido diretamente do publicador
        VIA_MULE     // Recebido via mula (para retransmitir)
    }

    public P2PAnnouncement() {
        this.receivedAt = new Date();
        this.retransmissionCount = 0;
        this.isPendingRetransmission = false;
    }

    public P2PAnnouncement(String id, String title, String description) {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderDeviceId() {
        return senderDeviceId;
    }

    public void setSenderDeviceId(String senderDeviceId) {
        this.senderDeviceId = senderDeviceId;
    }

    public Date getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(Date receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Date getWindowStart() {
        return windowStart;
    }

    public void setWindowStart(Date windowStart) {
        this.windowStart = windowStart;
    }

    public Date getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Date windowEnd) {
        this.windowEnd = windowEnd;
    }

    public boolean isAuthentic() {
        return isAuthentic;
    }

    public void setAuthentic(boolean authentic) {
        isAuthentic = authentic;
    }

    public ReceivedType getReceivedType() {
        return receivedType;
    }

    public void setReceivedType(ReceivedType receivedType) {
        this.receivedType = receivedType;
    }

    public int getRetransmissionCount() {
        return retransmissionCount;
    }

    public void setRetransmissionCount(int retransmissionCount) {
        this.retransmissionCount = retransmissionCount;
    }

    public void incrementRetransmissionCount() {
        this.retransmissionCount++;
    }

    public boolean isPendingRetransmission() {
        return isPendingRetransmission;
    }

    public void setPendingRetransmission(boolean pendingRetransmission) {
        isPendingRetransmission = pendingRetransmission;
    }

    public String getTargetDeviceId() {
        return targetDeviceId;
    }

    public void setTargetDeviceId(String targetDeviceId) {
        this.targetDeviceId = targetDeviceId;
    }

    /**
     * Verifica se o anúncio ainda está dentro da janela temporal
     */
    public boolean isActive() {
        Date now = new Date();
        if (windowStart != null && windowEnd != null) {
            return now.after(windowStart) && now.before(windowEnd);
        }
        return true; // Se não tem janela, considera ativo
    }

    /**
     * Retorna badge text baseado no tipo de recebimento
     */
    public String getReceivedTypeBadge() {
        if (receivedType == ReceivedType.DIRECT) {
            return "Direto";
        } else if (receivedType == ReceivedType.VIA_MULE) {
            return "Via Mula";
        }
        return "";
    }
}
