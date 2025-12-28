package com.plateforme.dons.service.interfaces;

import com.plateforme.dons.dto.payload.AnnonceRequest;
import com.plateforme.dons.dto.response.AnnonceResponse;
import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnonceService {
    AnnonceResponse createAnnonce(AnnonceRequest annonceRequest, String username);

    AnnonceResponse getAnnonceById(Long id);

    Page<AnnonceResponse> getAllAnnonces(Pageable pageable);

    AnnonceResponse updateAnnonce(Long id, AnnonceRequest annonceRequest, String username);

    void deleteAnnonce(Long id, String username);

    Page<AnnonceResponse> rechercherAnnonces(RechercheAnnonceCriteria criteria, Pageable pageable);

    java.util.List<String> getSuggestions(String query);
}
