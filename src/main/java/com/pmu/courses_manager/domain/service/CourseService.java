package com.pmu.courses_manager.domain.service;


import com.pmu.courses_manager.application.exception.CourseExisteDejaException;
import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.model.PartantId;
import com.pmu.courses_manager.domain.port.in.CourseManagementUseCase;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import com.pmu.courses_manager.domain.port.out.OutboxEventPersistencePort;
import com.pmu.courses_manager.domain.port.out.PartantPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Service d'application pour la gestion des courses
 */
@Service
public class CourseService implements CourseManagementUseCase {

    private final CoursePersistencePort coursePersistencePort;
    private final OutboxEventPersistencePort outboxEventPersistencePort;
    private final PartantPersistencePort participantPersistencePort;

    public CourseService(CoursePersistencePort coursePersistencePort, OutboxEventPersistencePort outboxEventPersistencePort, PartantPersistencePort participantPersistencePort) {
        this.coursePersistencePort = coursePersistencePort;
        this.outboxEventPersistencePort = outboxEventPersistencePort;
        this.participantPersistencePort = participantPersistencePort;
    }

    @Override
    @Transactional
    public CourseId createCourse(String nom, LocalDate date, Integer numero, List<String> partants) {
        if (coursePersistencePort.existsByDateAndNumero(date, numero)) {
            throw new CourseExisteDejaException(
                    "Une course avec la date " + date + " et le numéro " + numero + " existe déjà");
        }
        if (partants == null || partants.size() < 3) {
            throw new IllegalArgumentException("Une course doit avoir au minimum 3 partants");
        }
        Course course = new Course(nom, date, numero, partants);
        course.assignPartantNumber();
        Course savedCourse = coursePersistencePort.save(course);
        outboxEventPersistencePort.saveCreatedCourseEvent(savedCourse);
        return savedCourse.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourseById(CourseId courseId) {
        return coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));
    }

    @Override
    @Transactional
    public Partant addParticipant(CourseId courseId, String nom) {
        if (!coursePersistencePort.existsById(courseId)) {
            throw new CourseInexistanteException("Course non trouvée avec l'id : " + courseId);
        }
        int numero = getNextParticipantNumber(courseId);
        Partant partant = Partant.create(nom, numero);

        Partant savedParticipant = participantPersistencePort.save(courseId, partant);
        outboxEventPersistencePort.saveAddedParticipantEvent(courseId, savedParticipant);
        return savedParticipant;
    }

    private Integer getNextParticipantNumber(CourseId courseId) {
        Course course = coursePersistencePort.findById(courseId)
                .orElseThrow(() -> new CourseInexistanteException("Course non trouvée avec l'id : " + courseId));
        if (course.getPartants().isEmpty()) {
            return 1;
        }
        return course.getPartants().stream()
                .map(Partant::getNumero)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
    }
}

