package com.aplm.gdois.anunciosloc.anunciosloc.repository;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findBySessionToken(String token);
    void deleteBySessionToken(String token);
}