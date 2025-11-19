package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.UserProfile;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-profile")
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserProfileRepository repo;

    // Ver o meu perfil completo
    @GetMapping("/me")
    public List<UserProfile> meuPerfil(@RequestParam Long userId) {
        return repo.findByUserId(userId);
    }

    // Atualizar ou criar um valor (ex: profissão = professor)
    @PostMapping
    public ResponseEntity<?> atualizar(@RequestBody UserProfile profile) {
        profile.setUpdatedAt(java.time.LocalDateTime.now());
        repo.save(profile);
        return ResponseEntity.ok(profile);
    }

    // Apagar um campo do perfil
    @DeleteMapping
    public ResponseEntity<?> apagar(@RequestParam Long userId, @RequestParam Long keyId) {
        repo.deleteByUserIdAndKeyId(userId, keyId);
        return ResponseEntity.ok().build();
    }
}