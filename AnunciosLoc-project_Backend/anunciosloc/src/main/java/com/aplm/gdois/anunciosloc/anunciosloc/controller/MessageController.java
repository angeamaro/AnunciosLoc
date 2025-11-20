package com.aplm.gdois.anunciosloc.anunciosloc.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Message;
import com.aplm.gdois.anunciosloc.anunciosloc.service.MessageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // VER MENSAGENS DO LOCAL (com políticas aplicadas)
    @GetMapping("/location/{locationId}")
    public List<Message> doLocal(@PathVariable Long locationId, HttpServletRequest request) {
        Long viewerId = (Long) request.getAttribute("authenticatedUserId");
        if (viewerId == null) return List.of();
        return messageService.doLocal(locationId, viewerId);
    }

    // CRIAR MENSAGEM
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Message message, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        if (userId == null) return ResponseEntity.status(401).body("Login necessário");
        try {
            Message criada = messageService.criar(message, userId);
            return ResponseEntity.ok(criada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // APAGAR MENSAGEM
    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable UUID id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        if (userId == null) return ResponseEntity.status(401).body("Login necessário");
        try {
            messageService.apagar(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // MINHAS MENSAGENS
    @GetMapping("/minhas")
    public List<Message> minhas(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("authenticatedUserId");
        return userId != null ? messageService.minhas(userId) : List.of();
    }
}