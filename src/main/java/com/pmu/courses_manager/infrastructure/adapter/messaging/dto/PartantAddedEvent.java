package com.pmu.courses_manager.infrastructure.adapter.messaging.dto;

public record PartantAddedEvent (Long courseId, Long partantId, String nom, Integer numero){}
