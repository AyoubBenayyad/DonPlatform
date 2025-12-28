package com.plateforme.dons.dto.payload;

import com.plateforme.dons.entity.EtatObjet;
import com.plateforme.dons.entity.ModeRemise;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RechercheAnnonceCriteria {
    private String motCle;
    private String zoneGeographique;
    private EtatObjet etat;
    private ModeRemise modeRemise;
    private LocalDate dateMin;
    private LocalDate dateMax;
}
