package ao.co.isptec.aplm.anunciosloc.data.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ao.co.isptec.aplm.anunciosloc.data.model.Announcement;
import ao.co.isptec.aplm.anunciosloc.data.model.PolicyRule;
import ao.co.isptec.aplm.anunciosloc.utils.Constants;

/**
 * Repositório mock para gerenciar anúncios
 */
public class AnnouncementRepository {
    
    private static AnnouncementRepository instance;
    private final Map<String, Announcement> announcementsDatabase;
    
    private AnnouncementRepository() {
        announcementsDatabase = new HashMap<>();
        initializeMockData();
    }
    
    public static synchronized AnnouncementRepository getInstance() {
        if (instance == null) {
            instance = new AnnouncementRepository();
        }
        return instance;
    }
    
    /**
     * Inicializa dados mockados
     */
    private void initializeMockData() {
        Calendar calendar = Calendar.getInstance();
        
        // Anúncio 1: Evento para estudantes
        Announcement ann1 = new Announcement("1", "Workshop de Programação", 
            "Venha aprender Java e Android! Inscrições abertas para estudantes.",
            "3", "2");
        ann1.setLocationName("ISPTEC");
        ann1.setAuthorName("Bob Santos");
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        ann1.setEndDate(calendar.getTime());
        ann1.setDeliveryPolicy(Constants.POLICY_WHITELIST);
        ann1.addPolicyRule(new PolicyRule("profissao", "Estudante"));
        announcementsDatabase.put(ann1.getId(), ann1);
        
        // Anúncio 2: Promoção para todos
        calendar = Calendar.getInstance();
        Announcement ann2 = new Announcement("2", "Promoção de Verão",
            "Descontos em todas as lojas! Válido até o fim do mês.",
            "5", "3");
        ann2.setLocationName("Belas Shopping");
        ann2.setAuthorName("Carol Lima");
        calendar.add(Calendar.DAY_OF_MONTH, 15);
        ann2.setEndDate(calendar.getTime());
        ann2.setDeliveryPolicy(Constants.POLICY_EVERYONE);
        announcementsDatabase.put(ann2.getId(), ann2);
        
        // Anúncio 3: Evento desportivo
        calendar = Calendar.getInstance();
        Announcement ann3 = new Announcement("3", "Torneio de Futebol",
            "Inscreva sua equipe no torneio comunitário. Amantes de desporto bem-vindos!",
            "2", "3");
        ann3.setLocationName("Marginal de Luanda");
        ann3.setAuthorName("Carol Lima");
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        ann3.setEndDate(calendar.getTime());
        ann3.setDeliveryPolicy(Constants.POLICY_WHITELIST);
        ann3.addPolicyRule(new PolicyRule("interesse", "Desporto"));
        announcementsDatabase.put(ann3.getId(), ann3);
        
        // Anúncio 4: Cultural
        calendar = Calendar.getInstance();
        Announcement ann4 = new Announcement("4", "Visita Guiada Histórica",
            "Conheça a história de Luanda através dos seus monumentos.",
            "4", "1");
        ann4.setLocationName("Fortaleza de São Miguel");
        ann4.setAuthorName("Alice Silva");
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        ann4.setEndDate(calendar.getTime());
        ann4.setDeliveryPolicy(Constants.POLICY_EVERYONE);
        announcementsDatabase.put(ann4.getId(), ann4);
        
        // Anúncio 5: Evento bloqueado para professores
        calendar = Calendar.getInstance();
        Announcement ann5 = new Announcement("5", "Festa Estudantil",
            "Grande festa para estudantes! Professores não permitidos.",
            "1", "1");
        ann5.setLocationName("Largo da Independência");
        ann5.setAuthorName("Alice Silva");
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        ann5.setEndDate(calendar.getTime());
        ann5.setDeliveryPolicy(Constants.POLICY_BLACKLIST);
        ann5.addPolicyRule(new PolicyRule("profissao", "Professor"));
        announcementsDatabase.put(ann5.getId(), ann5);
        
        // Anúncio 6: Tecnologia
        calendar = Calendar.getInstance();
        Announcement ann6 = new Announcement("6", "Hackathon 2025",
            "48 horas de código, inovação e prêmios! Interessados em tecnologia, participem!",
            "3", "2");
        ann6.setLocationName("ISPTEC");
        ann6.setAuthorName("Bob Santos");
        calendar.add(Calendar.DAY_OF_MONTH, 10);
        ann6.setEndDate(calendar.getTime());
        ann6.setDeliveryPolicy(Constants.POLICY_WHITELIST);
        ann6.addPolicyRule(new PolicyRule("interesse", "Tecnologia"));
        announcementsDatabase.put(ann6.getId(), ann6);
    }
    
    /**
     * Cria novo anúncio
     */
    public Announcement createAnnouncement(Announcement announcement) {
        if (announcement.getId() == null || announcement.getId().isEmpty()) {
            announcement.setId(UUID.randomUUID().toString());
        }
        announcement.setCreatedAt(System.currentTimeMillis());
        announcement.setUpdatedAt(System.currentTimeMillis());
        announcement.setStatus(Constants.STATUS_ACTIVE);
        announcementsDatabase.put(announcement.getId(), announcement);
        return announcement;
    }
    
    /**
     * Obtém todos os anúncios
     */
    public List<Announcement> getAllAnnouncements() {
        return new ArrayList<>(announcementsDatabase.values());
    }
    
    /**
     * Obtém anúncios ativos
     */
    public List<Announcement> getActiveAnnouncements() {
        List<Announcement> activeAnnouncements = new ArrayList<>();
        for (Announcement announcement : announcementsDatabase.values()) {
            if (announcement.isActive()) {
                activeAnnouncements.add(announcement);
            }
        }
        return activeAnnouncements;
    }
    
    /**
     * Obtém anúncio por ID
     */
    public Announcement getAnnouncementById(String announcementId) {
        return announcementsDatabase.get(announcementId);
    }
    
    /**
     * Obtém anúncios de um local específico
     */
    public List<Announcement> getAnnouncementsByLocation(String locationId) {
        List<Announcement> locationAnnouncements = new ArrayList<>();
        for (Announcement announcement : announcementsDatabase.values()) {
            if (locationId.equals(announcement.getLocationId()) && announcement.isActive()) {
                locationAnnouncements.add(announcement);
            }
        }
        return locationAnnouncements;
    }
    
    /**
     * Obtém anúncios criados por um usuário
     */
    public List<Announcement> getAnnouncementsByUser(String userId) {
        List<Announcement> userAnnouncements = new ArrayList<>();
        for (Announcement announcement : announcementsDatabase.values()) {
            if (userId.equals(announcement.getAuthorId())) {
                userAnnouncements.add(announcement);
            }
        }
        return userAnnouncements;
    }
    
    /**
     * Obtém anúncios filtrados por política de entrega
     */
    public List<Announcement> getAnnouncementsForUser(Map<String, String> userAttributes) {
        List<Announcement> filteredAnnouncements = new ArrayList<>();
        for (Announcement announcement : announcementsDatabase.values()) {
            if (announcement.isActive() && announcement.canUserReceive(userAttributes)) {
                filteredAnnouncements.add(announcement);
            }
        }
        return filteredAnnouncements;
    }
    
    /**
     * Atualiza anúncio
     */
    public boolean updateAnnouncement(Announcement announcement) {
        if (announcement == null || announcement.getId() == null) {
            return false;
        }
        
        if (!announcementsDatabase.containsKey(announcement.getId())) {
            return false;
        }
        
        announcement.setUpdatedAt(System.currentTimeMillis());
        announcementsDatabase.put(announcement.getId(), announcement);
        return true;
    }
    
    /**
     * Remove anúncio
     */
    public boolean deleteAnnouncement(String announcementId) {
        if (announcementId == null) {
            return false;
        }
        
        Announcement announcement = announcementsDatabase.get(announcementId);
        if (announcement != null) {
            announcement.setStatus(Constants.STATUS_EXPIRED);
            return true;
        }
        return false;
    }
    
    /**
     * Remove anúncio permanentemente
     */
    public boolean permanentlyDeleteAnnouncement(String announcementId) {
        return announcementsDatabase.remove(announcementId) != null;
    }
    
    /**
     * Incrementa contador de visualizações
     */
    public void incrementViewCount(String announcementId) {
        Announcement announcement = announcementsDatabase.get(announcementId);
        if (announcement != null) {
            announcement.incrementViewCount();
        }
    }
    
    /**
     * Busca anúncios por título ou conteúdo
     */
    public List<Announcement> searchAnnouncements(String query) {
        List<Announcement> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        
        for (Announcement announcement : announcementsDatabase.values()) {
            if (announcement.getTitle().toLowerCase().contains(lowerQuery) ||
                announcement.getContent().toLowerCase().contains(lowerQuery)) {
                results.add(announcement);
            }
        }
        return results;
    }
    
    /**
     * Obtém anúncios recentes (últimos 7 dias)
     */
    public List<Announcement> getRecentAnnouncements() {
        List<Announcement> recentAnnouncements = new ArrayList<>();
        long weekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000);
        
        for (Announcement announcement : announcementsDatabase.values()) {
            if (announcement.getCreatedAt() >= weekAgo && announcement.isActive()) {
                recentAnnouncements.add(announcement);
            }
        }
        return recentAnnouncements;
    }
}
