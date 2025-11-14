package com.aplm.gdois.anunciosloc.anunciosloc.controller;


import com.aplm.gdois.anunciosloc.anunciosloc.entity.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.User;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // 1. Verifica se o username já existe
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            // 2. Se existe, devolve um erro 409 Conflict
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Erro: O nome de utilizador já está em uso.");
        }
        // 3. Se não existe, encripta a senha e salva
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        User savedUser = userRepo.save(user);
        
        // 4. Devolve o utilizador criado com um status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        User user = userRepo.findByUsername(loginRequest.getUsername()).orElse(null);

        if (user != null && encoder.matches(loginRequest.getPasswordHash(), user.getPasswordHash())) {
            // Sucesso: cria um token e o objeto de resposta
            String token = java.util.UUID.randomUUID().toString();
            LoginResponse response = new LoginResponse(user, token);
            return ResponseEntity.ok(response); // Retorna 200 OK com o objeto JSON
        } else {
            // Falha: retorna um erro 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }
}