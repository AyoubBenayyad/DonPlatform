package com.plateforme.dons.service.impl;

import com.plateforme.dons.dto.response.AnnonceResponse;
import com.plateforme.dons.dto.response.UserSummaryDto;
import com.plateforme.dons.entity.Annonce;
import com.plateforme.dons.entity.User;
import com.plateforme.dons.exception.ResourceNotFoundException;
import com.plateforme.dons.repository.AnnonceRepository;
import com.plateforme.dons.repository.UserRepository;
import com.plateforme.dons.service.interfaces.FavoriService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriServiceImpl implements FavoriService {

    private final UserRepository userRepository;
    private final AnnonceRepository annonceRepository;

    @Override
    @Transactional
    public void addFavori(Long annonceId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce", "id", annonceId));

        user.getFavoris().add(annonce);
        userRepository.save(user); // JPA handles the relationship update
    }

    @Override
    @Transactional
    public void removeFavori(Long annonceId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce", "id", annonceId));

        user.getFavoris().remove(annonce);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnonceResponse> getFavoris(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Note: For large datasets, fetching all and paging in memory is bad.
        // Better to use a Repository method but for a Set default implementation this
        // works for now.
        // Ideally: annonceRepository.findFavorisByUserId(userId, pageable)

        List<AnnonceResponse> favoris = user.getFavoris().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), favoris.size());

        if (start > favoris.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, favoris.size());
        }

        return new PageImpl<>(favoris.subList(start, end), pageable, favoris.size());
    }

    private AnnonceResponse mapToResponse(Annonce annonce) {
        AnnonceResponse response = new AnnonceResponse();
        response.setId(annonce.getId());
        response.setTitre(annonce.getTitre());
        response.setDescription(annonce.getDescription());
        response.setEtat(annonce.getEtat());
        response.setDatePublication(annonce.getDatePublication());
        response.setZoneGeographique(annonce.getZoneGeographique());
        response.setModeRemise(annonce.getModeRemise());
        response.setStatut(annonce.getStatut());
        response.setMotsCles(annonce.getMotsCles());
        response.setImages(annonce.getImages());

        UserSummaryDto userSummary = new UserSummaryDto();
        userSummary.setId(annonce.getCreateur().getId());
        userSummary.setUsername(annonce.getCreateur().getUsername());
        userSummary.setEmail(annonce.getCreateur().getEmail());
        response.setCreateur(userSummary);

        return response;
    }
}
