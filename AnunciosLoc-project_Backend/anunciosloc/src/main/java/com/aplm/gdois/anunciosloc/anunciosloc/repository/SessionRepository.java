package com.aplm.gdois.anunciosloc.anunciosloc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Session;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Session s WHERE s.userId = :userId")
    void deleteByUserId(UUID userId);

    void deleteBySessionToken(String token);
}
