package com.pmu.courses_manager.infrastructure.adapter.persistence;

import com.pmu.courses_manager.infrastructure.adapter.persistence.entities.PartantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository JPA pour les entités Course
 */
interface PartantJpaRepository extends JpaRepository<PartantJpaEntity, Long> {}


