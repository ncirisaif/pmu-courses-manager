package com.pmu.courses_manager.application.api.request;

import java.time.LocalDate;
import java.util.List;

public record CreateCourseRequest(
        String nom,
        LocalDate date,
        Integer numero,
        List<String> partants
) {}