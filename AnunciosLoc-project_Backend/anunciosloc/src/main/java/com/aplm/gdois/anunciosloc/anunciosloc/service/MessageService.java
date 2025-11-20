package com.aplm.gdois.anunciosloc.anunciosloc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Message;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.MessagePolicy;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.UserProfile;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.MessagePolicyRepository;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.MessageRepository;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserProfileRepository;

import jakarta.transaction.Transactional;

@Service
public class MessageService {

    @Autowired private MessageRepository messageRepo;
    @Autowired private MessagePolicyRepository policyRepo;
    @Autowired private UserProfileRepository profileRepo;

    @Transactional
    public Message criar(Message message, Long userId) {
        if (message.getContent() == null || message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("A mensagem não pode estar vazia");
        }
        if (message.getLocationId() == null) {
            throw new IllegalArgumentException("É necessário escolher um local");
        }

        message.setPublisherId(userId);
        message.setCreatedAt(LocalDateTime.now());
        message.setDeletedAt(null);

        return messageRepo.save(message);
    }

    // Mensagens visíveis num determinado local (aplica whitelist/blacklist)
    public List<Message> doLocal(Long locationId, Long viewerId) {
        List<Message> mensagens = messageRepo.findByLocationIdAndDeletedAtIsNull(locationId);

        return mensagens.stream()
                .filter(msg -> podeVer(msg, viewerId))
                .toList();
    }

    @Transactional
    public void apagar(UUID messageId, Long userId) {
        Message msg = messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));

        if (!msg.getPublisherId().equals(userId)) {
            throw new SecurityException("Apenas o autor pode apagar a mensagem");
        }

        msg.setDeletedAt(LocalDateTime.now());
        messageRepo.save(msg);
    }

    public List<Message> minhas(Long userId) {
        return messageRepo.findByPublisherIdAndDeletedAtIsNull(userId);
    }

    // WHITELIST / BLACKLIST – funciona com keyId (exatamente como tens nas tabelas)
   private boolean podeVer(Message msg, Long viewerId) {
    String policy = msg.getPolicyType() != null ? msg.getPolicyType().toLowerCase() : "none";

    if ("none".equals(policy)) return true;

    List<UserProfile> perfilDoViewer = profileRepo.findByUserId(viewerId);
    List<MessagePolicy> regrasDaMensagem = policyRepo.findByMessageId(msg.getId());

    if (regrasDaMensagem.isEmpty()) return true;

    boolean cumpreTodasAsRegras = regrasDaMensagem.stream().allMatch(regra ->
        perfilDoViewer.stream().anyMatch(p ->
            p.getKeyId().equals(regra.getKeyId()) && 
            p.getValue().equals(regra.getValue())
        )
    );

    return "whitelist".equals(policy) ? cumpreTodasAsRegras : !cumpreTodasAsRegras;
}
}