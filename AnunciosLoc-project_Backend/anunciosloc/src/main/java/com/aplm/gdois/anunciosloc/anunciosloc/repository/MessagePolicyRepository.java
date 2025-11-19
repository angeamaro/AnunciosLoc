package com.aplm.gdois.anunciosloc.anunciosloc.repository;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.MessagePolicy;
import com.aplm.gdois.anunciosloc.anunciosloc.entity.MessagePolicyId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MessagePolicyRepository extends JpaRepository<MessagePolicy, MessagePolicyId> {

    List<MessagePolicy> findByMessageId(UUID messageId);
    void deleteByMessageId(UUID messageId);
}