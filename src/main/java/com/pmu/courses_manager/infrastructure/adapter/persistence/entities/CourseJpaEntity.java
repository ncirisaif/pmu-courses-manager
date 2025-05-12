package com.pmu.courses_manager.infrastructure.adapter.persistence.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * Entité JPA pour Course
 */
@Entity
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(name = "uk_date_numero", columnNames = {"date_course", "numero"})
})
@Setter
@Getter
@NoArgsConstructor
public class CourseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "date_course", nullable = false)
    private LocalDate date;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<PartantJpaEntity> partants = new HashSet<>();


    public void addParticipant(PartantJpaEntity participant) {
        partants.add(participant);
        participant.setCourse(this);
    }
}
