package com.pmu.courses_manager.infrastructure.adapter.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox")
@Setter
@Getter
@NoArgsConstructor
public class OutboxEvent {

    @Id
    private UUID id;
    private String topic;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
    private LocalDateTime createdAt;
    private boolean sent = false;}