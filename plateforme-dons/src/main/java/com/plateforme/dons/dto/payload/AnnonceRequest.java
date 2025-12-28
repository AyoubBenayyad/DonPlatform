package com.plateforme.dons.dto.payload;

import com.plateforme.dons.entity.EtatObjet;
import com.plateforme.dons.entity.ModeRemise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class AnnonceRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "L'état de l'objet est obligatoire")
    private EtatObjet etat;

    @NotBlank(message = "La zone géographique est obligatoire")
    private String zoneGeographique;

    @NotNull(message = "Le mode de remise est obligatoire")
    private ModeRemise modeRemise;

    private Set<String> motsCles;
    private Set<String> images;
}
