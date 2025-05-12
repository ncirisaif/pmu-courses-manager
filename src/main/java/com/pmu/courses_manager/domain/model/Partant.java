package com.pmu.courses_manager.domain.model;

import java.util.Objects;

/**
 * Entité Participant du domaine
 */
public class Partant {
    private PartantId id;
    private String nom;
    private Integer numero;

    public Partant() {
    }

    public void setId(PartantId id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    private Partant(String nom, Integer numero) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du participant ne peut pas être vide");
        }
        this.nom = nom;
        this.numero = numero;
    }
    private Partant(String nom, Integer numero, Course course) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du partant ne peut pas être vide");
        }
        if (course == null) {
            throw new IllegalArgumentException("La course ne peut pas être nulle");
        }

        this.nom = nom;
        this.numero = numero;
    }

    public static Partant create(String nom, Integer numero) {
        return new Partant(nom, numero);
    }
    public static Partant create(String nom, Integer numero, Course course) {
        return new Partant(nom, numero, course);
    }

    public PartantId getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Integer getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partant that = (Partant) o;
        return Objects.equals(nom, that.nom) &&
                Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, numero);
    }
    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", numero=" + numero +
                '}';
    }
}