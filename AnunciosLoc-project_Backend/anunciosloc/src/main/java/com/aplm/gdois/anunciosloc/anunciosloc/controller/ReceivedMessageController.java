package com.aplm.gdois.anunciosloc.anunciosloc.controller;


import com.aplm.gdois.anunciosloc.anunciosloc.entity.ReceivedMessage;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.ReceivedMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/received-messages")
@CrossOrigin(origins = "*")
public class ReceivedMessageController {

    @Autowired
    private ReceivedMessageRepository repo;

    // 1. Marcar como recebida (quando o user entra no local)
    @PostMapping("/received")
    public ResponseEntity<?> marcarComoRecebida(
            @RequestBody MarkReceivedRequest req) {

        if (repo.existsByMessageIdAndUserId(req.messageId(), req.userId())) {
            return ResponseEntity.ok().build(); // já existe
        }

        ReceivedMessage rm = new ReceivedMessage(req.messageId(), req.userId());
        repo.save(rm);
        return ResponseEntity.ok(rm);
    }

    // 2. Marcar como lida
    @PostMapping("/read")
    public ResponseEntity<?> marcarComoLida(@RequestBody MarkReadRequest req) {
        return repo.findByMessageIdAndUserId(req.messageId(), req.userId())
                .map(rm -> {
                    rm.setReadAt(LocalDateTime.now());
                    repo.save(rm);
                    return ResponseEntity.ok(rm);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Ver todas as mensagens que um user recebeu
    @GetMapping("/user/{userId}")
    public List<ReceivedMessage> doUser(@PathVariable Long userId) {
        return repo.findByUserId(userId);
    }
}

// DTOs simples e limpos
record MarkReceivedRequest(UUID messageId, Long userId) {}
record MarkReadRequest(UUID messageId, Long userId) {}