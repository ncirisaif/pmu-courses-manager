package com.pmu.courses_manager.infrastructure.adapter.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.port.out.OutboxEventPersistencePort;
import com.pmu.courses_manager.infrastructure.adapter.messaging.PartantAddedEvent;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.OutboxEvent;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class OutboxEventJpaAdapter implements OutboxEventPersistencePort{
    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventJpaAdapter(OutboxJpaRepository outboxJpaRepository, ObjectMapper objectMapper) {
        this.outboxJpaRepository = outboxJpaRepository;
        this.objectMapper = objectMapper;
    }
    @Override
    public OutboxEvent saveAddedParticipantEvent(CourseId courseId, Partant participant) {
        PartantAddedEvent event = new PartantAddedEvent(
                courseId.getValue(),
                participant.getId().getValue(),
                participant.getNom(),
                participant.getNumero());
        try {
            String payload = objectMapper.writeValueAsString(event);
            OutboxEvent outbox = new OutboxEvent();
            outbox.setId(UUID.randomUUID());
            outbox.setTopic("participant-added");
            outbox.setPayload(payload);
            outbox.setCreatedAt(LocalDateTime.now());
            return outboxJpaRepository.save(outbox);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public OutboxEvent saveCreatedCourseEvent(Course course) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(course);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        OutboxEvent outbox = new OutboxEvent();
        outbox.setId(UUID.randomUUID());
        outbox.setTopic("course-created");
        outbox.setPayload(payload);
        outbox.setCreatedAt(LocalDateTime.now());
        return outboxJpaRepository.save(outbox);
    }

}
