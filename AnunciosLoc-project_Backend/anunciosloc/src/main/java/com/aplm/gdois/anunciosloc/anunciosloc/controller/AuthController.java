package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Session;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.User;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.SessionRepository;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private SessionRepository sessionRepo;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // REGISTRO (melhorado)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByUsername(request.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Username já existe");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        userRepo.save(user);

        return ResponseEntity.ok("Utilizador registado com sucesso");
    }

    // LOGIN COM SESSÃO REAL NA BASE DE DADOS
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepo.findByUsername(request.username())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        // Apaga sessões antigas do user (opcional, mas recomendado)
        sessionRepo.findAll().stream()
                .filter(s -> s.getUserId().equals(user.getId()))
                .forEach(sessionRepo::delete);

        // Cria nova sessão
        Session session = new Session();
        session.setUserId(user.getId());
        session.setSessionToken(UUID.randomUUID().toString());
        session.setExpiresAt(LocalDateTime.now().plusDays(7)); // 7 dias de validade
        sessionRepo.save(session);

        // Resposta bonita e útil para o frontend
        return ResponseEntity.ok(Map.of(
            "message", "Login com sucesso",
            "token", session.getSessionToken(),
            "userId", user.getId(),
            "username", user.getUsername(),
            "expiresAt", session.getExpiresAt().toString()
        ));
    }

    // LOGOUT (apaga a sessão)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token ausente");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        sessionRepo.deleteBySessionToken(token);

        return ResponseEntity.ok("Logout com sucesso");
    }
}

// DTOs limpos e seguros (podes deixar aqui ou na pasta dto)
record RegisterRequest(String username, String password) {}
record LoginRequest(String username, String password) {}