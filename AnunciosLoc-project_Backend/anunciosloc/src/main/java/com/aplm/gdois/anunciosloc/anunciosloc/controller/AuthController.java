package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Session;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.User;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.SessionRepository;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private SessionRepository sessionRepo;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    // REGISTRO
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

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepo.findByUsername(request.username()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        // Apaga sessões antigas do usuário
        sessionRepo.deleteByUserId(user.getId());

        // Cria nova sessão
        Session session = new Session();
        session.setUserId(user.getId());
        session.setSessionToken(UUID.randomUUID().toString());
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        session.setCreatedAt(LocalDateTime.now());
        sessionRepo.save(session);

        return ResponseEntity.ok(Map.of(
                "message", "Login com sucesso",
                "token", session.getSessionToken(),
                "userId", user.getId(),
                "username", user.getUsername(),
                "expiresAt", session.getExpiresAt().toString()
        ));
    }

    // LOGOUT
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

// DTOs
record RegisterRequest(String username, String password) {}
record LoginRequest(String username, String password) {}
