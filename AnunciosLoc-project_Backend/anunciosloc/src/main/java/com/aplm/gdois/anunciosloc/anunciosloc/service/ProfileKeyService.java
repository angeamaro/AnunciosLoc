package com.aplm.gdois.anunciosloc.anunciosloc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.ProfileKey;
import com.aplm.gdois.anunciosloc.anunciosloc.repository.ProfileKeyRepository;

@Service
public class ProfileKeyService {

    @Autowired
    private ProfileKeyRepository repo;

    // Listar todas as chaves disponíveis
    public List<ProfileKey> listarTodas() {
        return repo.findAll();
    }

    // Buscar por ID
    public Optional<ProfileKey> buscarPorId(Long id) {
        return repo.findById(id);
    }

    // Buscar por nome da chave (útil no frontend)
    public Optional<ProfileKey> buscarPorNome(String keyName) {
        return repo.findByKeyName(keyName);
    }

    // Criar nova chave (ex: "curso", "ano", "gosta_futebol")
    @Transactional
    public ProfileKey criar(String keyName) {
        keyName = keyName.trim().toLowerCase();

        if (keyName.isEmpty()) {
            throw new IllegalArgumentException("O nome da chave não pode estar vazio");
        }
        if (keyName.length() > 50) {
            throw new IllegalArgumentException("O nome da chave não pode ter mais de 50 caracteres");
        }
        if (repo.existsByKeyName(keyName)) {
            throw new IllegalArgumentException("Já existe uma chave com o nome: " + keyName);
        }

        ProfileKey nova = new ProfileKey();
        nova.setKeyName(keyName);
        return repo.save(nova);
    }

    // Apagar chave (só se não estiver em uso — opcional, mas seguro)
    @Transactional
    public void apagar(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Chave não encontrada com id: " + id);
        }
        // Aqui podes adicionar verificação se está em uso em UserProfile ou MessagePolicy
        repo.deleteById(id);
    }
}