package com.plateforme.dons.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public EtatObjet getEtat() {
        return etat;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public String getZoneGeographique() {
        return zoneGeographique;
    }

    public ModeRemise getModeRemise() {
        return modeRemise;
    }

    public StatutAnnonce getStatut() {
        return statut;
    }

    public Set<String> getMotsCles() {
        return motsCles;
    }

    public Set<String> getImages() {
        return images;
    }

    public User getCreateur() {
        return createur;
    }

    public void setCreateur(User createur) {
        this.createur = createur;
    }

    public void setStatut(StatutAnnonce statut) {
        this.statut = statut;
    }

    public Set<User> getFavorisParUsers() {
        return favorisParUsers;
    }

    public void setFavorisParUsers(Set<User> favorisParUsers) {
        this.favorisParUsers = favorisParUsers;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEtat(EtatObjet etat) {
        this.etat = etat;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public void setZoneGeographique(String zoneGeographique) {
        this.zoneGeographique = zoneGeographique;
    }

    public void setModeRemise(ModeRemise modeRemise) {
        this.modeRemise = modeRemise;
    }

    public void setMotsCles(Set<String> motsCles) {
        this.motsCles = motsCles;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    @PrePersist
    protected void onCreate() {
        datePublication = LocalDateTime.now();
        if (statut == null) {
            statut = StatutAnnonce.DISPONIBLE;
        }
    }
}
