package com.plateforme.dons.service.interfaces;

import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import com.plateforme.dons.entity.RechercheSauvegarde;

import java.util.List;

public interface RechercheSauvegardeService {
    RechercheSauvegarde saveSearch(RechercheAnnonceCriteria criteria, String title, String username);

    List<RechercheSauvegarde> getUserSearches(String username);

    void deleteSearch(Long id, String username);
}
