package com.pmu.courses_manager.infrastructure.adapter.persistence;


import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.port.out.PartantPersistencePort;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.CourseJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.PartantJpaEntity;
import com.pmu.courses_manager.infrastructure.adapter.persistence.mapper.PartantMapper;
import org.springframework.stereotype.Service;

/**
 * Adaptateur de persistance pour les partants (JPA)
 */
@Service
public class PartantJpaAdapter implements PartantPersistencePort {

    private final PartantJpaRepository participantRepository;
    private final CourseJpaRepository courseJpaRepository;

    private final PartantMapper partantMapper;

    public PartantJpaAdapter(
            PartantJpaRepository participantRepository, CourseJpaRepository courseJpaRepository, PartantMapper partantMapper) {
        this.participantRepository = participantRepository;
        this.courseJpaRepository = courseJpaRepository;
        this.partantMapper = partantMapper;
    }

    @Override
    public Partant save(CourseId courseId, Partant partant) {
        CourseJpaEntity courseJpaEntity = courseJpaRepository.findById(courseId.getValue())
                .orElseThrow(() -> new IllegalStateException("Course non trouvée avec l'id : " + courseId));

        PartantJpaEntity partantJpaEntity = partantMapper.toEntity(partant, courseJpaEntity);
        partantJpaEntity.setCourse(courseJpaEntity);
        PartantJpaEntity savedEntity = participantRepository.save(partantJpaEntity);
        return partantMapper.toDomain(savedEntity);
    }
}


