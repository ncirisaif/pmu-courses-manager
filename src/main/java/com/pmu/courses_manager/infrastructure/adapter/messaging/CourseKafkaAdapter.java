package com.pmu.courses_manager.infrastructure.adapter.messaging;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pmu.courses_manager.domain.model.Course;
import com.pmu.courses_manager.domain.model.CourseId;
import com.pmu.courses_manager.domain.model.Partant;
import com.pmu.courses_manager.domain.port.out.CourseEventPort;
import com.pmu.courses_manager.infrastructure.adapter.persistence.OutboxJpaRepository;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptateur Kafka pour la publication des événements
 */
@Component
public class CourseKafkaAdapter implements CourseEventPort {

    private static final Logger logger = LoggerFactory.getLogger(CourseKafkaAdapter.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OutboxJpaRepository outboxJpaRepository;

    // Noms des topics Kafka
    private static final String COURSE_CREATED_TOPIC = "course-created";
    private static final String PARTICIPANT_ADDED_TOPIC = "participant-added";

    public CourseKafkaAdapter(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, OutboxJpaRepository outboxJpaRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.outboxJpaRepository = outboxJpaRepository;
    }

    @Override
    public void publishCourseCreated(Course course) {
        try {
            String payload = objectMapper.writeValueAsString(course);
            kafkaTemplate.send(COURSE_CREATED_TOPIC, course.getId().toString(), payload);
            logger.info("Event published: CourseCreated - {}", course.getId());
        } catch (Exception e) {
            logger.error("Failed to publish CourseCreated event", e);
            throw new EventPublicationException("Failed to publish CourseCreated event", e);
        }
    }

    @Override
    public void publishParticipantAdded(CourseId courseId, Partant participant) {
        try {
            PartantAddedEvent event = new PartantAddedEvent(
                    courseId.getValue(),
                    participant.getId().getValue(),
                    participant.getNom(),
                    participant.getNumero());

            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(PARTICIPANT_ADDED_TOPIC, courseId.toString(), payload);

            logger.info("Event published: ParticipantAdded - Course: {}, Participant: {}",
                    courseId, participant.getId());
        } catch (Exception e) {
            logger.error("Failed to publish ParticipantAdded event", e);
            throw new EventPublicationException("Failed to publish ParticipantAdded event", e);
        }
    }
    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents() {
        List<OutboxEvent> events = outboxJpaRepository.findAllBySentFalse();

        for (OutboxEvent event : events) {
            kafkaTemplate.send(event.getTopic(), event.getPayload());
            event.setSent(true);
        }
        outboxJpaRepository.saveAll(events);
    }
}



