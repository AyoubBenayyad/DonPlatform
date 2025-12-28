package com.plateforme.dons.service.interfaces;

import com.plateforme.dons.dto.response.AnnonceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriService {
    void addFavori(Long annonceId, String username);

    void removeFavori(Long annonceId, String username);

    Page<AnnonceResponse> getFavoris(String username, Pageable pageable);
}
