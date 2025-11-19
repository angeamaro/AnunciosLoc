package com.aplm.gdois.anunciosloc.anunciosloc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "message_policies")
@IdClass(MessagePolicyId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessagePolicy {

    @Id
    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Id
    @Column(name = "key_id", nullable = false)
    private Long keyId;

    @Id
    @Column(nullable = false)
    private String value;

    // Relacionamentos (só para navegação, não entram no construtor)
    @ManyToOne
    @JoinColumn(name = "message_id", insertable = false, updatable = false)
    private Message message;

    @ManyToOne
    @JoinColumn(name = "key_id", insertable = false, updatable = false)
    private ProfileKey profileKey;

    // CONSTRUTOR MANUAL – SÓ OS CAMPOS QUE PRECISAS!
    public MessagePolicy(UUID messageId, Long keyId, String value) {
        this.messageId = messageId;
        this.keyId = keyId;
        this.value = value;
    }
}