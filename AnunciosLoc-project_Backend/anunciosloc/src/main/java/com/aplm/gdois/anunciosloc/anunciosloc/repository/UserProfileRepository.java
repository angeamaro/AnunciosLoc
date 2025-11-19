package com.aplm.gdois.anunciosloc.anunciosloc.repository;


import com.aplm.gdois.anunciosloc.anunciosloc.entity.UserProfile;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.UserProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, UserProfileId> {

    // Todos os valores de um user
    List<UserProfile> findByUserId(Long userId);

    // Valor específico (ex: profissão do user 5)
    UserProfile findByUserIdAndKeyId(Long userId, Long keyId);

    // Apagar um valor específico
    void deleteByUserIdAndKeyId(Long userId, Long keyId);
}