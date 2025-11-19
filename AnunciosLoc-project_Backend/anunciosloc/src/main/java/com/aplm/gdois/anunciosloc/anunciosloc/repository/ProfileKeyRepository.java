package com.aplm.gdois.anunciosloc.anunciosloc.repository;


import com.aplm.gdois.anunciosloc.anunciosloc.entity.ProfileKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfileKeyRepository extends JpaRepository<ProfileKey, Long> {
    Optional<ProfileKey> findByKeyName(String keyName);
    boolean existsByKeyName(String keyName);
}