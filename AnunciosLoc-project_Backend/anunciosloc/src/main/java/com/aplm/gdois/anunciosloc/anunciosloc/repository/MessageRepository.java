package com.aplm.gdois.anunciosloc.anunciosloc.repository;

import com.aplm.gdois.anunciosloc.anunciosloc.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Todas as mensagens ativas de um local
    List<Message> findByLocationIdAndDeletedAtIsNull(Long locationId);

    // Só centralizadas ou só descentralizadas
    List<Message> findByDeliveryModeAndDeletedAtIsNull(String deliveryMode);

    // Mensagens do meu local que ainda estão no horário permitido
    List<Message> findByLocationIdAndDeletedAtIsNullAndTimeStartLessThanEqualAndTimeEndGreaterThanEqual(
            Long locationId, java.time.LocalDateTime now1, java.time.LocalDateTime now2);

    // Mensagens que eu publiquei
    List<Message> findByPublisherIdAndDeletedAtIsNull(Long publisherId);
}