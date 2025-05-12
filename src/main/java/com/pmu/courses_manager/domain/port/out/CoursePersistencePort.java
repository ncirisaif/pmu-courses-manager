package com.pmu.courses_manager.domain.port.out;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Port de sortie pour la persistance des courses
 */
public interface CoursePersistencePort {

    /**
     * Enregistre une course (création ou mise à jour)
     */
    Course save(Course course);

    /**
     * Supprime une course
     */
    void delete(CourseId courseId);

    /**
     * Charge une course par son identifiant
     */
    Optional<Course> findById(CourseId courseId);

    /**
     * Vérifie si une course existe avec la date et le numéro donnés
     */
    boolean existsByDateAndNumero(LocalDate date, Integer numero);

    /**
     * Vérifie si une course existe avec un identifiant
     */
    boolean existsById(CourseId courseId);
}
