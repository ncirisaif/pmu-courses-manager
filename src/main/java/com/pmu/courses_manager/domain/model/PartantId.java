package com.pmu.courses_manager.domain.model;

import java.util.Objects;

/**
 * Value Object pour l'identifiant d'un participant
 */
public class PartantId {
    private final Long value;

    private PartantId() {
        this.value = null;
    }

    public PartantId(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("L'identifiant de participant ne peut pas être null");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartantId that = (PartantId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
