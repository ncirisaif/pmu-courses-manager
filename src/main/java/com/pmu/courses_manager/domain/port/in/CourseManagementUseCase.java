package com.pmu.courses_manager.domain.port.in;

import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;

import java.time.LocalDate;
import java.util.List;

/**
 * Port d'entrée pour la gestion des courses
 */
public interface CourseManagementUseCase {

    /**
     * Crée une nouvelle course
     */
    CourseId createCourse(String nom, LocalDate date, Integer numero, List<String> partants);

    /**
     * Récupère une course par son identifiant
     */
    Course getCourseById(CourseId courseId);

    /**
     * Ajoute un participant à une course
     */
    Partant addParticipant(CourseId courseId, String nom);

}