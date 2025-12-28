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
}
