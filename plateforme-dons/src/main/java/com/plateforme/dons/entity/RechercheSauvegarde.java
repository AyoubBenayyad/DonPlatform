package com.plateforme.dons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recherches_sauvegardees")
public class RechercheSauvegarde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    private LocalDateTime dateCreation;

    // Search Criteria Fields
    private String motCle;
    private String zoneGeographique;

    @Enumerated(EnumType.STRING)
    private EtatObjet etat;

    @Enumerated(EnumType.STRING)
    private ModeRemise modeRemise;

    private LocalDate dateMin;
    private LocalDate dateMax;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
