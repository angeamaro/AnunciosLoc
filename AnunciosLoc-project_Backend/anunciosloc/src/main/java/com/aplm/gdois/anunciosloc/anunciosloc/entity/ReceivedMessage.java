package com.aplm.gdois.anunciosloc.anunciosloc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "received_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReceivedMessage {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "message_id", nullable = false, updatable = false)
    private UUID messageId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "received_at")
    private LocalDateTime receivedAt = LocalDateTime.now();

    @Column(name = "read_at")
    private LocalDateTime readAt;

    // Relacionamentos (opcional, mas bonito)
    @ManyToOne
    @JoinColumn(name = "message_id", insertable = false, updatable = false)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Construtor útil
    public ReceivedMessage(UUID messageId, Long userId) {
        this.messageId = messageId;
        this.userId = userId;
    }
}