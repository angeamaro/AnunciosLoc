package com.aplm.gdois.anunciosloc.anunciosloc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile_keys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_name", nullable = false, unique = true, length = 50)
    private String keyName;

    // Construtor útil (opcional)
    public ProfileKey(String keyName) {
        this.keyName = keyName;
    }
}