package com.pmu.courses_manager.unit;

import com.pmu.courses_manager.application.exception.CourseExisteDejaException;
import com.pmu.courses_manager.application.exception.CourseInexistanteException;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.port.out.CourseEventPort;
import com.pmu.courses_manager.domain.port.out.CoursePersistencePort;
import com.pmu.courses_manager.domain.port.out.OutboxEventPersistencePort;
import com.pmu.courses_manager.domain.port.out.PartantPersistencePort;
import com.pmu.courses_manager.domain.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service CourseService
 */
@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CoursePersistencePort coursePersistencePort;

    @Mock
    private CourseEventPort courseEventPort;

    @Mock
    private OutboxEventPersistencePort outboxEventPersistencePort;
    @Mock
    PartantPersistencePort participantPersistencePort;

    private CourseService courseService;

    private static final CourseId ID_VALIDE = new CourseId(1L);
    private static final String NOM_VALIDE = "Course de Test";
    private static final LocalDate DATE_VALIDE = LocalDate.of(2025, 5, 5);
    private static final Integer NUMERO_VALIDE = 1;
    private static final List<String> PARTANTS_VALIDES = List.of("Cheval 1", "Cheval 2", "Cheval 3");

    @BeforeEach
    void setUp() {
        courseService = new CourseService(coursePersistencePort, outboxEventPersistencePort, participantPersistencePort);
    }

    @Nested
    @DisplayName("Tests de création de course")
    class CreateCourseTests {

        @Test
        @DisplayName("Doit créer une course avec succès")
        void shouldCreateCourseSuccessfully() {
            // Given
            Course course = new Course(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE, PARTANTS_VALIDES);
            course.setId(ID_VALIDE);
            when(coursePersistencePort.existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE)).thenReturn(false);
            when(coursePersistencePort.save(course)).thenReturn(course);

            // When
            CourseId result = courseService.createCourse(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE, PARTANTS_VALIDES);

            // Then
            assertNotNull(result);
            verify(coursePersistencePort).existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE);
            verify(coursePersistencePort).save(course);
            verify(outboxEventPersistencePort).saveCreatedCourseEvent(course);
        }

        @Test
        @DisplayName("Doit rejeter une course avec date et numéro déjà existants")
        void shouldRejectCourseWithExistingDateAndNumero() {
            when(coursePersistencePort.existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE)).thenReturn(true);

            assertThrows(CourseExisteDejaException.class, () -> {
                courseService.createCourse(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE, PARTANTS_VALIDES);
            });

            verify(coursePersistencePort).existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE);
            verify(coursePersistencePort, never()).save(any());
            verify(outboxEventPersistencePort, never()).saveCreatedCourseEvent(any());
        }

        @Test
        @DisplayName("Doit rejeter une course avec moins de 3 partants")
        void shouldRejectCourseWithLessThanThreePartants() {
            List<String> partantsInvalides = List.of("Cheval 1", "Cheval 2");

            when(coursePersistencePort.existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE)).thenReturn(false);

            assertThrows(IllegalArgumentException.class, () -> {
                courseService.createCourse(NOM_VALIDE, DATE_VALIDE, NUMERO_VALIDE, partantsInvalides);
            });

            verify(coursePersistencePort).existsByDateAndNumero(DATE_VALIDE, NUMERO_VALIDE);
            verify(coursePersistencePort, never()).save(any());
            verify(outboxEventPersistencePort, never()).saveCreatedCourseEvent(any());
        }
    }


    @Nested
    @DisplayName("Tests des requêtes sur les courses")
    class CourseQueryTests {

        @Test
        @DisplayName("Doit récupérer une course par son ID")
        void shouldGetCourseById() {
            CourseId courseId = new CourseId(1L);
            Course course = mock(Course.class);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.of(course));

            Course result = courseService.getCourseById(courseId);

            assertNotNull(result);
            assertEquals(course, result);
        }

        @Test
        @DisplayName("Doit lever une exception si la course n'existe pas")
        void shouldThrowExceptionIfCourseNotFound() {
            CourseId courseId = new CourseId(999L);
            when(coursePersistencePort.findById(courseId)).thenReturn(Optional.empty());

            assertThrows(CourseInexistanteException.class, () -> {
                courseService.getCourseById(courseId);
            });
        }
    }
}