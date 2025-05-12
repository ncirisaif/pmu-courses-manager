package com.pmu.courses_manager.infrastructure.adapter.persistence.mapper;

import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.model.PartantId;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.PartantJpaEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PartantMapper {
    PartantId map(Long id) {
        return id != null ? new PartantId(id) : null;
    }

    public abstract Partant toDomain(PartantJpaEntity partantJpaEntity);

    public PartantJpaEntity toEntity(Partant partant, CourseJpaEntity parent) {
        if (partant == null) return null;

        PartantJpaEntity entity = new PartantJpaEntity();
        entity.setNom(partant.getNom());
        entity.setNumero(partant.getNumero());
        entity.setCourse(parent);

        return entity;
    }



}
