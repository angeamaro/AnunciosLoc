package com.aplm.gdois.anunciosloc.anunciosloc.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserProfileId implements Serializable {

    private Long userId;
    private Long keyId;

    // Construtores
    public UserProfileId() {}

    public UserProfileId(Long userId, Long keyId) {
        this.userId = userId;
        this.keyId = keyId;
    }

    // equals e hashCode (OBRIGATÓRIO!)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfileId that = (UserProfileId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(keyId, that.keyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, keyId);
    }
}