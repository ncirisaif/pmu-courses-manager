package com.pmu.courses_manager.application.api;


import com.pmu.courses_manager.application.api.request.CreateCourseRequest;
import com.pmu.courses_manager.application.api.request.AddPartantRequest;
import com.pmu.courses_manager.application.api.response.CreateCourseResponse;
import com.pmu.courses_manager.application.api.dto.PartantDto;
import com.pmu.courses_manager.application.mapper.ApplicationMapper;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.port.in.CourseManagementUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des courses
 * Implémente l'interface CourseApi pour la documentation OpenAPI
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseResources implements CourseResourcesApi {

    private final CourseManagementUseCase courseManagementUseCase;
    private final ApplicationMapper mapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping
    public ResponseEntity<CreateCourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest courseRequestDto) {
        CourseId courseId = courseManagementUseCase.createCourse(
                courseRequestDto.nom(),
                courseRequestDto.date(),
                courseRequestDto.numero(),
                courseRequestDto.partants());

        Course course = courseManagementUseCase.getCourseById(courseId);
        return new ResponseEntity<>(mapper.toResponseDto(course), HttpStatus.CREATED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CreateCourseResponse> getCourseById(@PathVariable Long id) {
        Course course = courseManagementUseCase.getCourseById(new CourseId(id));
        return ResponseEntity.ok(mapper.toResponseDto(course));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping("/{courseId}/partants")
    public ResponseEntity<PartantDto> addParticipant(
            @PathVariable Long courseId,
            @Valid @RequestBody AddPartantRequest request) {

        Partant partant = courseManagementUseCase.addParticipant(
                new CourseId(courseId),
                request.nom());
        return new ResponseEntity<>(mapper.toDto(partant), HttpStatus.CREATED);
    }
}