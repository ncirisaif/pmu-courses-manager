package com.pmu.courses_manager.infrastructure.adapter.persistence.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA pour Participant
 */
@Entity
@Table(name = "partants", uniqueConstraints = {
        @UniqueConstraint(name = "uk_course_dossard", columnNames = {"course_id", "dossard"})
})
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PartantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "dossard", nullable = false)
    private Integer numero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private CourseJpaEntity course;
}