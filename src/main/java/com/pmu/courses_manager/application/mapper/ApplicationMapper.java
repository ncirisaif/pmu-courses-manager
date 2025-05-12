package com.pmu.courses_manager.application.mapper;

import com.pmu.courses_manager.application.api.dto.PartantDto;
import com.pmu.courses_manager.application.api.response.CreateCourseResponse;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    default Long mapCourseIdToLong(CourseId courseId) {
        return courseId != null ? courseId.getValue() : null;
    }
    CreateCourseResponse toResponseDto(Course course);
    PartantDto toDto(Partant partant);

    default List<String> map(Set<Partant> partants) {
        if (partants == null) return null;
        return partants.stream()
                .map(Partant::getNom)
                .toList();
    }
}
