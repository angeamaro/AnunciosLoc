package com.aplm.gdois.anunciosloc.anunciosloc.service;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.MessagePolicy;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.MessagePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MessagePolicyService {

    @Autowired
    private MessagePolicyRepository repo;

    @Transactional
    public void salvarRegras(UUID messageId, List<MessagePolicy> regras) {
        // Apaga as antigas (se existirem)
        repo.deleteByMessageId(messageId);
        // Guarda as novas
        regras.forEach(regra -> regra.setMessageId(messageId));
        repo.saveAll(regras);
    }

    public List<MessagePolicy> buscarPorMensagem(UUID messageId) {
        return repo.findByMessageId(messageId);
    }
}