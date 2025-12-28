package com.plateforme.dons.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private EtatObjet etat;

    private LocalDateTime datePublication;

    private String zoneGeographique;

    @Enumerated(EnumType.STRING)
    private ModeRemise modeRemise;

    @Enumerated(EnumType.STRING)
    private StatutAnnonce statut;

    @ElementCollection
    @CollectionTable(name = "annonce_mots_cles", joinColumns = @JoinColumn(name = "annonce_id"))
    @Column(name = "mot_cle")
    @Builder.Default
    private Set<String> motsCles = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "annonce_images", joinColumns = @JoinColumn(name = "annonce_id"))
    @Column(name = "image_url")
    @Builder.Default
    private Set<String> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User createur;

    @ManyToMany(mappedBy = "favoris")
    @Builder.Default
    private Set<User> favorisParUsers = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        datePublication = LocalDateTime.now();
        if (statut == null) {
            statut = StatutAnnonce.DISPONIBLE;
        }
    }
}
