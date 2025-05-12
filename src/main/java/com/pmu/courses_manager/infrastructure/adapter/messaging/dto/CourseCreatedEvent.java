package com.pmu.courses_manager.infrastructure.adapter.messaging.dto;

import java.time.LocalDate;

/**
 * Classes d'événements (Event DTO)
 */
public record CourseCreatedEvent(Long courseId, String nom, LocalDate date, Integer numero) {}
