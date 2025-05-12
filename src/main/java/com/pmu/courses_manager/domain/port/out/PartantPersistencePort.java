package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.model.PartantId;

import java.util.Optional;

/**
 * Port de sortie pour la persistance des participant
 */
public interface PartantPersistencePort {

    /**
     * Enregistre une Participant
     */
    Partant save(CourseId courseId, Partant participant);

    /**
     * Charge un Participant par son identifiant
     */
    Optional<Partant> findById(PartantId partantId);
}
