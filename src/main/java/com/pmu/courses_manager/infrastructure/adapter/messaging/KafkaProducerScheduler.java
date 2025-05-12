package com.pmu.courses_manager.infrastructure.adapter.messaging;


import com.pmu.courses_manager.infrastructure.adapter.persistence.OutboxJpaRepository;
import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.OutboxEvent;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class KafkaProducerScheduler {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerScheduler.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxJpaRepository outboxJpaRepository;

    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents() {
        List<OutboxEvent> events = outboxJpaRepository.findAllBySentFalse();

        for (OutboxEvent event : events) {
            logger.info("Event: {}", event.toString()+" has been published!");
            kafkaTemplate.send(event.getTopic(), event.getPayload());
            event.setSent(true);
        }
        outboxJpaRepository.saveAll(events);
    }
}



