package com.plateforme.dons.dto.response;

import com.plateforme.dons.entity.EtatObjet;
import com.plateforme.dons.entity.ModeRemise;
import com.plateforme.dons.entity.StatutAnnonce;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class AnnonceResponse {
    private Long id;
    private String titre;
    private String description;
    private EtatObjet etat;
    private LocalDateTime datePublication;
    private String zoneGeographique;
    private ModeRemise modeRemise;
    private StatutAnnonce statut;
    private Set<String> motsCles;
    private Set<String> images;
    private UserSummaryDto createur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EtatObjet getEtat() {
        return etat;
    }

    public void setEtat(EtatObjet etat) {
        this.etat = etat;
    }

    public LocalDateTime getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDateTime datePublication) {
        this.datePublication = datePublication;
    }

    public String getZoneGeographique() {
        return zoneGeographique;
    }

    public void setZoneGeographique(String zoneGeographique) {
        this.zoneGeographique = zoneGeographique;
    }

    public ModeRemise getModeRemise() {
        return modeRemise;
    }

    public void setModeRemise(ModeRemise modeRemise) {
        this.modeRemise = modeRemise;
    }

    public StatutAnnonce getStatut() {
        return statut;
    }

    public void setStatut(StatutAnnonce statut) {
        this.statut = statut;
    }

    public Set<String> getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(Set<String> motsCles) {
        this.motsCles = motsCles;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public UserSummaryDto getCreateur() {
        return createur;
    }

    public void setCreateur(UserSummaryDto createur) {
        this.createur = createur;
    }
}
