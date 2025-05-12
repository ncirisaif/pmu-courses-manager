package com.pmu.courses_manager.domain.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Agrégat racine représentant une course.
 * Une course a lieu un jour donné et possède un nom et un numéro unique pour ce jour.
 * Une course possède au minimum 3 partants.
 */
public class Course {
    private CourseId id;
    private String nom;
    private LocalDate date;
    private Integer numero;
    private final Set<Partant> partants = new HashSet<>();

    public Course() {
    }

    public Course(String nom, LocalDate date, Integer numero, List<String> partantsNoms) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la course ne peut pas être vide");
        }
        if (date == null) {
            throw new IllegalArgumentException("La date de la course est obligatoire");
        }
        if (numero == null || numero <= 0) {
            throw new IllegalArgumentException("Le numéro de la course doit être un entier positif");
        }
        if (partantsNoms == null || partantsNoms.size() < 3) {
            throw new IllegalArgumentException("Une course doit avoir au minimum 3 partants");
        }
        for (String partantNom : partantsNoms) {
            this.addParticipant(partantNom);
        }
        this.nom = nom;
        this.date = date;
        this.numero = numero;
    }

    public void addParticipant(String partantNom) {
        int numero = partants.size() + 1;
        Partant partant = Partant.create(partantNom, numero, this);
        partants.add(partant);
    }

    public void assignPartantNumber() {
        int PartantNumber = 1;
        for (Partant partant : this.partants) {
            partant.setNumero(PartantNumber++);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(date, course.date) &&
                Objects.equals(numero, course.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, numero);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                ", numero=" + numero +
                '}';
    }

    public void setId(CourseId id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    public CourseId getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getNumero() {
        return numero;
    }

    public Set<Partant> getPartants() {
        return partants;
    }
}