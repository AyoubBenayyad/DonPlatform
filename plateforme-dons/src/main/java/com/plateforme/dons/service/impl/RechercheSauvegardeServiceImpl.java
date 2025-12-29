package com.plateforme.dons.service.impl;

import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import com.plateforme.dons.entity.RechercheSauvegarde;
import com.plateforme.dons.entity.User;
import com.plateforme.dons.exception.ResourceNotFoundException;
import com.plateforme.dons.repository.RechercheSauvegardeRepository;
import com.plateforme.dons.repository.UserRepository;
import com.plateforme.dons.service.interfaces.RechercheSauvegardeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RechercheSauvegardeServiceImpl implements RechercheSauvegardeService {

    private final RechercheSauvegardeRepository rechercheSauvegardeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RechercheSauvegarde saveSearch(RechercheAnnonceCriteria criteria, String title, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        RechercheSauvegarde savedSearch = RechercheSauvegarde.builder()
                .titre(title)
                .user(user)
                .motCle(criteria.getMotCle())
                .zoneGeographique(criteria.getZoneGeographique())
                .etat(criteria.getEtat())
                .modeRemise(criteria.getModeRemise())
                .dateMin(criteria.getDateMin())
                .dateMax(criteria.getDateMax())
                .build();

        return rechercheSauvegardeRepository.save(savedSearch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RechercheSauvegarde> getUserSearches(String username) {
        return rechercheSauvegardeRepository.findByUserUsername(username);
    }

    @Override
    @Transactional
    public void deleteSearch(Long id, String username) {
        RechercheSauvegarde search = rechercheSauvegardeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RechercheSauvegarde", "id", id));

        if (!search.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Accès refusé: Cette recherche n'appartient pas à l'utilisateur");
        }

        rechercheSauvegardeRepository.delete(search);
    }
}
