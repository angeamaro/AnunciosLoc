package ao.co.isptec.aplm.anunciosloc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import ao.co.isptec.aplm.anunciosloc.data.model.P2PAnnouncement;

/**
 * Gerenciador de anúncios P2P recebidos
 * Armazena em SharedPreferences (em produção, usar SQLite)
 */
public class P2PAnnouncementManager {
    private static final String PREFS_NAME = "p2p_announcements";
    private static final String KEY_ANNOUNCEMENTS = "announcements_list";
    private static P2PAnnouncementManager instance;
    private Context context;
    private Gson gson;

    private P2PAnnouncementManager(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();
    }

    public static synchronized P2PAnnouncementManager getInstance(Context context) {
        if (instance == null) {
            instance = new P2PAnnouncementManager(context);
        }
        return instance;
    }

    /**
     * Salva um novo anúncio P2P recebido
     */
    public void saveAnnouncement(P2PAnnouncement announcement) {
        List<P2PAnnouncement> announcements = getAllAnnouncements();
        
        // Evita duplicatas
        for (int i = 0; i < announcements.size(); i++) {
            if (announcements.get(i).getId().equals(announcement.getId())) {
                announcements.set(i, announcement);
                saveAllAnnouncements(announcements);
                return;
            }
        }
        
        announcements.add(0, announcement); // Adiciona no início
        saveAllAnnouncements(announcements);
    }

    /**
     * Retorna todos os anúncios P2P
     */
    public List<P2PAnnouncement> getAllAnnouncements() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_ANNOUNCEMENTS, null);
        
        if (json != null) {
            Type listType = new TypeToken<List<P2PAnnouncement>>(){}.getType();
            return gson.fromJson(json, listType);
        }
        return new ArrayList<>();
    }

    /**
     * Retorna apenas anúncios recebidos diretamente
     */
    public List<P2PAnnouncement> getDirectAnnouncements() {
        List<P2PAnnouncement> all = getAllAnnouncements();
        List<P2PAnnouncement> direct = new ArrayList<>();
        for (P2PAnnouncement ann : all) {
            if (ann.getReceivedType() == P2PAnnouncement.ReceivedType.DIRECT) {
                direct.add(ann);
            }
        }
        return direct;
    }

    /**
     * Retorna apenas anúncios pendentes de retransmissão (mula)
     */
    public List<P2PAnnouncement> getMuleAnnouncements() {
        List<P2PAnnouncement> all = getAllAnnouncements();
        List<P2PAnnouncement> mule = new ArrayList<>();
        for (P2PAnnouncement ann : all) {
            if (ann.getReceivedType() == P2PAnnouncement.ReceivedType.VIA_MULE 
                && ann.isPendingRetransmission()) {
                mule.add(ann);
            }
        }
        return mule;
    }

    /**
     * Busca um anúncio pelo ID
     */
    public P2PAnnouncement getAnnouncementById(String announcementId) {
        List<P2PAnnouncement> announcements = getAllAnnouncements();
        for (P2PAnnouncement ann : announcements) {
            if (ann.getId().equals(announcementId)) {
                return ann;
            }
        }
        return null;
    }

    /**
     * Incrementa contador de retransmissão
     */
    public void incrementRetransmissionCount(String announcementId) {
        List<P2PAnnouncement> announcements = getAllAnnouncements();
        for (P2PAnnouncement ann : announcements) {
            if (ann.getId().equals(announcementId)) {
                ann.incrementRetransmissionCount();
                if (ann.getRetransmissionCount() >= 1) {
                    ann.setPendingRetransmission(false); // Já foi retransmitido
                }
                break;
            }
        }
        saveAllAnnouncements(announcements);
    }

    /**
     * Retorna contadores de estatísticas
     */
    public int getTotalCount() {
        return getAllAnnouncements().size();
    }

    public int getDirectCount() {
        return getDirectAnnouncements().size();
    }

    public int getMuleCount() {
        return getMuleAnnouncements().size();
    }

    /**
     * Limpa todos os anúncios P2P
     */
    public void clearAll() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    private void saveAllAnnouncements(List<P2PAnnouncement> announcements) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(announcements);
        prefs.edit().putString(KEY_ANNOUNCEMENTS, json).apply();
    }
}
