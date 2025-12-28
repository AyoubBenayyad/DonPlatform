package com.plateforme.dons.service.impl;

import com.plateforme.dons.dto.payload.AnnonceRequest;
import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import com.plateforme.dons.dto.response.AnnonceResponse;
import com.plateforme.dons.dto.response.UserSummaryDto;
import com.plateforme.dons.entity.Annonce;
import com.plateforme.dons.entity.Role;
import com.plateforme.dons.entity.User;
import com.plateforme.dons.exception.ResourceNotFoundException;
import com.plateforme.dons.repository.AnnonceRepository;
import com.plateforme.dons.repository.UserRepository;
import com.plateforme.dons.repository.spec.AnnonceSpecifications;
import com.plateforme.dons.service.interfaces.AnnonceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AnnonceResponse createAnnonce(AnnonceRequest annonceRequest, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + username));

        Annonce annonce = new Annonce();
        // annonce.Titre(annonceRequest.getTitre()); // Lombok setter fix needed? No,
        // using data
        annonce.setTitre(annonceRequest.getTitre());
        annonce.setDescription(annonceRequest.getDescription());
        annonce.setEtat(annonceRequest.getEtat());
        annonce.setZoneGeographique(annonceRequest.getZoneGeographique());
        annonce.setModeRemise(annonceRequest.getModeRemise());
        annonce.setMotsCles(annonceRequest.getMotsCles());
        annonce.setImages(annonceRequest.getImages());
        annonce.setCreateur(user);

        Annonce savedAnnonce = annonceRepository.save(annonce);
        return mapToResponse(savedAnnonce);
    }

    @Override
    public AnnonceResponse getAnnonceById(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce", "id", id));
        return mapToResponse(annonce);
    }

    @Override
    public Page<AnnonceResponse> getAllAnnonces(Pageable pageable) {
        Page<Annonce> annonces = annonceRepository.findAll(pageable);
        return annonces.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public AnnonceResponse updateAnnonce(Long id, AnnonceRequest annonceRequest, String username) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce", "id", id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!annonce.getCreateur().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier cette annonce");
        }

        annonce.setTitre(annonceRequest.getTitre());
        annonce.setDescription(annonceRequest.getDescription());
        annonce.setEtat(annonceRequest.getEtat());
        annonce.setZoneGeographique(annonceRequest.getZoneGeographique());
        annonce.setModeRemise(annonceRequest.getModeRemise());
        annonce.setMotsCles(annonceRequest.getMotsCles());

        Annonce updatedAnnonce = annonceRepository.save(annonce);
        return mapToResponse(updatedAnnonce);
    }

    @Override
    @Transactional
    public void deleteAnnonce(Long id, String username) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce", "id", id));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!annonce.getCreateur().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer cette annonce");
        }

        annonceRepository.delete(annonce);
    }

    @Override
    public Page<AnnonceResponse> rechercherAnnonces(RechercheAnnonceCriteria criteria, Pageable pageable) {
        System.out.println("⚡ [Backend] Searching annonces with criteria: " + criteria);
        if (criteria.getMotCle() != null)
            System.out.println("   - Mot clé: " + criteria.getMotCle());
        if (criteria.getZoneGeographique() != null)
            System.out.println("   - Zone: " + criteria.getZoneGeographique());

        Specification<Annonce> spec = AnnonceSpecifications.withCriteria(criteria);
        Page<Annonce> annonces = annonceRepository.findAll(spec, pageable);
        System.out.println("✅ [Backend] Found " + annonces.getTotalElements() + " matching annonces.");

        return annonces.map(this::mapToResponse);
    }

    @Override
    public java.util.List<String> getSuggestions(String query) {
        if (query == null || query.trim().length() < 2) {
            return java.util.Collections.emptyList();
        }
        return annonceRepository.findSuggestions(query.trim());
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
