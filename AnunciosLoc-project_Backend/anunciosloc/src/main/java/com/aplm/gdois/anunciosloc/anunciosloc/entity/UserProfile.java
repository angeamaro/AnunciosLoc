package com.aplm.gdois.anunciosloc.anunciosloc.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_profiles")
@IdClass(UserProfileId.class)  // Chave composta: user_id + key_id
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserProfile {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "key_id", nullable = false)
    private Long keyId;

    @Column(nullable = false)
    private String value;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Relacionamentos (opcional, mas deixa mais bonito)
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "key_id", insertable = false, updatable = false)
    private ProfileKey profileKey;
}