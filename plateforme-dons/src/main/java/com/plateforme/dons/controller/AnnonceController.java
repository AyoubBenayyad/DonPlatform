package com.plateforme.dons.controller;

import com.plateforme.dons.dto.payload.AnnonceRequest;
import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import com.plateforme.dons.dto.response.AnnonceResponse;
import com.plateforme.dons.service.interfaces.AnnonceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/annonces")
@RequiredArgsConstructor
public class AnnonceController {

    private final AnnonceService annonceService;
    private final com.plateforme.dons.service.interfaces.StorageService storageService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createAnnonce(
            @RequestPart("annonce") String annonceRequestStr,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            AnnonceRequest annonceRequest = mapper.readValue(annonceRequestStr, AnnonceRequest.class);

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = storageService.uploadFile(imageFile);
                annonceRequest.setImages(java.util.Collections.singleton(imageUrl));
            } else {
                return ResponseEntity.badRequest().body("Une image est obligatoire pour créer une annonce.");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AnnonceResponse createdAnnonce = annonceService.createAnnonce(annonceRequest, username);
            return new ResponseEntity<>(createdAnnonce, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'annonce", e);
        }
    }

    @GetMapping
    public ResponseEntity<Page<AnnonceResponse>> getAllAnnonces(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(annonceService.getAllAnnonces(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceResponse> getAnnonceById(@PathVariable Long id) {
        System.out.println("⚡ [Backend] Request received for Annonce ID: " + id);
        try {
            AnnonceResponse response = annonceService.getAnnonceById(id);
            System.out.println("✅ [Backend] Annonce found: " + response.getTitre());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ [Backend] Error fetching annonce: " + e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceResponse> updateAnnonce(@PathVariable Long id,
            @Valid @RequestBody AnnonceRequest annonceRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(annonceService.updateAnnonce(id, annonceRequest, username));
    }

    @GetMapping("/recherche")
    public ResponseEntity<Page<AnnonceResponse>> rechercherAnnonces(RechercheAnnonceCriteria criteria,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(annonceService.rechercherAnnonces(criteria, pageable));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<java.util.List<String>> getSuggestions(@RequestParam String query) {
        return ResponseEntity.ok(annonceService.getSuggestions(query));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        annonceService.deleteAnnonce(id, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<java.util.List<AnnonceResponse>> getMyAnnonces() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(annonceService.getAnnoncesByUsername(username));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<AnnonceResponse> updateStatut(@PathVariable Long id,
            @RequestParam com.plateforme.dons.entity.StatutAnnonce statut) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(annonceService.updateStatut(id, statut, username));
    }
}
