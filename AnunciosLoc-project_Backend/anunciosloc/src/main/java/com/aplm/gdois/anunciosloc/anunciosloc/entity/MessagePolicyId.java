package com.aplm.gdois.anunciosloc.anunciosloc.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class MessagePolicyId implements Serializable {

    private UUID messageId;
    private Long keyId;
    private String value;

    public MessagePolicyId() {}

    public MessagePolicyId(UUID messageId, Long keyId, String value) {
        this.messageId = messageId;
        this.keyId = keyId;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagePolicyId that = (MessagePolicyId) o;
        return Objects.equals(messageId, that.messageId) &&
               Objects.equals(keyId, that.keyId) &&
               Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, keyId, value);
    }
}