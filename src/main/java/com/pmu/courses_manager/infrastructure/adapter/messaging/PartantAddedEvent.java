package com.pmu.courses_manager.infrastructure.adapter.messaging;

public class PartantAddedEvent {
    private Long courseId;
    private Long partantId;
    private String nom;
    private Integer dossard;

    public PartantAddedEvent() {}

    public PartantAddedEvent(Long courseId, Long participantId, String nom, Integer dossard) {
        this.courseId = courseId;
        this.partantId = participantId;
        this.nom = nom;
        this.dossard = dossard;
    }

    // Getters et setters
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getPartantId() { return partantId; }
    public void setPartantId(Long partantId) { this.partantId = partantId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getDossard() { return dossard; }
    public void setDossard(Integer dossard) { this.dossard = dossard; }
}
