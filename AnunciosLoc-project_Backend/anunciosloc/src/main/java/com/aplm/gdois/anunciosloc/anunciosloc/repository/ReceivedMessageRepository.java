package com.aplm.gdois.anunciosloc.anunciosloc.repository;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.ReceivedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceivedMessageRepository extends JpaRepository<ReceivedMessage, UUID> {

    Optional<ReceivedMessage> findByMessageIdAndUserId(UUID messageId, Long userId);
    List<ReceivedMessage> findByUserId(Long userId);
    List<ReceivedMessage> findByMessageId(UUID messageId);
    boolean existsByMessageIdAndUserId(UUID messageId, Long userId);
}