package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.MessagePolicy;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.MessagePolicyRepository;
import com.aplm.gdois.anunciosloc.anunciosloc.dto.PolicyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages/{messageId}/policies")
@CrossOrigin(origins = "*")
public class MessagePolicyController {

    @Autowired
    private MessagePolicyRepository policyRepo;

    // Ver políticas atuais
    @GetMapping
    public List<MessagePolicy> listar(@PathVariable UUID messageId) {
        return policyRepo.findByMessageId(messageId);
    }

    // Substituir todas as políticas (mais simples e seguro)
    @PostMapping
    public ResponseEntity<?> atualizar(
            @PathVariable UUID messageId,
            @RequestBody List<PolicyRequest> policies) {

        // Apaga as antigas
        policyRepo.deleteByMessageId(messageId);

        // Cria as novas
        for (PolicyRequest p : policies) {
            MessagePolicy mp = new MessagePolicy(messageId, p.keyId(), p.value());
            policyRepo.save(mp);
        }

        return ResponseEntity.ok().build();
    }
}