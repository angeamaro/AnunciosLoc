package com.aplm.gdois.anunciosloc.anunciosloc.controller;


import com.aplm.gdois.anunciosloc.anunciosloc.dto.LoginRequest;
import com.aplm.gdois.anunciosloc.anunciosloc.dto.LoginResponse;
import com.aplm.gdois.anunciosloc.anunciosloc.dto.RegisterRequest;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.User;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Validação
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username não pode estar vazio");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password não pode estar vazio");
        }
        
        // Verificar se usuário já existe
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já existe");
        }
        
        // Criar novo usuário
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(encoder.encode(request.getPassword()));
        userRepo.save(user);
        
        return ResponseEntity.ok("Usuário registrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Validação
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("Username e password são obrigatórios");
        }
        
        User user = userRepo.findByUsername(request.getUsername()).orElse(null);
        if (user != null && encoder.matches(request.getPassword(), user.getPasswordHash())) {
            String token = java.util.UUID.randomUUID().toString();
            LoginResponse response = new LoginResponse(token, user.getUsername());
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }
}