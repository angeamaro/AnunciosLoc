package com.aplm.gdois.anunciosloc.anunciosloc.controller;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.ProfileKey;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.ProfileKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile-keys")
@CrossOrigin(origins = "*")
public class ProfileKeyController {

    @Autowired
    private ProfileKeyRepository repo;

    // Listar todas as chaves públicas (ex: profissão, esporte, idade, curso, etc)
    @GetMapping
    public List<ProfileKey> listarTodas() {
        return repo.findAll();
    }

    // Criar nova chave (ex: "profissão", "esporte", "gosta_de")
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ProfileKey key) {
        if (repo.existsByKeyName(key.getKeyName())) {
            return ResponseEntity.badRequest().body("Chave já existe: " + key.getKeyName());
        }
        ProfileKey salva = repo.save(key);
        return ResponseEntity.ok(salva);
    }

    // Apagar chave (cuidado! só se não estiver em uso)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}