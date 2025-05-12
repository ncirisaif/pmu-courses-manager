package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;

/**
 * Port de sortie pour la persistance des participant
 */
public interface PartantPersistencePort {

    /**
     * Enregistre une Participant
     */
    Partant save(CourseId courseId, Partant participant);
}
