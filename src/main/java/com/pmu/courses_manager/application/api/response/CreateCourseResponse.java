package com.pmu.courses_manager.application.api.response;

import com.pmu.courses_manager.application.api.dto.PartantDto;

import java.time.LocalDate;
import java.util.List;

public record CreateCourseResponse(
        Long id,
        String nom,
        LocalDate date,
        Integer numero,
        List<PartantDto> partants
) {}