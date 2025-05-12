package com.pmu.courses_manager.infrastructure.adapter.persistence.mapper;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.PartantJpaEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PartantMapper.class)
public abstract class CourseMapper {

    @Autowired
    protected PartantMapper partantMapper;

    public CourseJpaEntity toEntity(Course course) {
        if (course == null) return null;

        CourseJpaEntity entity = new CourseJpaEntity();
        entity.setNom(course.getNom());
        entity.setDate(course.getDate());
        entity.setNumero(course.getNumero());

        if (course.getPartants() != null) {
            Set<PartantJpaEntity> partants = course.getPartants().stream()
                    .map(p -> partantMapper.toEntity(p, entity))
                    .collect(Collectors.toSet());
            entity.setPartants(partants);
        }

        return entity;
    }

    public CourseId map(Long value) {
        return value != null ? new CourseId(value) : null;
    }
    public abstract Course toDomain(CourseJpaEntity courseJpaEntity);

}
