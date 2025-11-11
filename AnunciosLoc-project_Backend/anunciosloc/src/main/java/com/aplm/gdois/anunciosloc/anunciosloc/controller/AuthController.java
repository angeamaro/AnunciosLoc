package com.aplm.gdois.anunciosloc.anunciosloc.controller;


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
    public User register(@RequestBody User user) {
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        return userRepo.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User login) {
        User user = userRepo.findByUsername(login.getUsername()).orElse(null);
        if (user != null && encoder.matches(login.getPasswordHash(), user.getPasswordHash())) {
            return "Login OK - Session: " + java.util.UUID.randomUUID();
        }
        throw new RuntimeException("Credenciais inválidas");
    }
}