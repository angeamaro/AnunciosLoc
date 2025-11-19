package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Message;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.MessageRepository;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageRepository repo;

    // 1. Listar mensagens de um local (o mais importante!)
    @GetMapping("/location/{locationId}")
    public List<Message> doLocal(@PathVariable Long locationId) {
        return repo.findByLocationIdAndDeletedAtIsNull(locationId);
    }

    // 2. Criar nova mensagem
    @PostMapping
    public Message criar(@RequestBody Message message) {
        return repo.save(message);
    }

    // 3. Apagar (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable UUID id) {
        return repo.findById(id)
                .map(msg -> {
                    msg.setDeletedAt(LocalDateTime.now());
                    repo.save(msg);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Minhas mensagens publicadas
    @GetMapping("/minhas")
    public List<Message> minhas(@RequestParam Long publisherId) {
        return repo.findByPublisherIdAndDeletedAtIsNull(publisherId);
    }
}
