package com.plateforme.dons.web.controller;

import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import com.plateforme.dons.entity.RechercheSauvegarde;
import com.plateforme.dons.service.interfaces.RechercheSauvegardeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recherches")
@RequiredArgsConstructor
@Tag(name = "Recherches Sauvegardées", description = "API de gestion des recherches sauvegardées")
@SecurityRequirement(name = "bearerAuth")
public class SavedSearchController {

    private final RechercheSauvegardeService rechercheSauvegardeService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Sauvegarder une recherche")
    public ResponseEntity<RechercheSauvegarde> saveSearch(
            @RequestBody RechercheAnnonceCriteria criteria,
            @RequestParam String title,
            Authentication authentication) {

        RechercheSauvegarde saved = rechercheSauvegardeService.saveSearch(criteria, title, authentication.getName());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les recherches de l'utilisateur connecté")
    public ResponseEntity<List<RechercheSauvegarde>> getUserSearches(Authentication authentication) {
        return ResponseEntity.ok(rechercheSauvegardeService.getUserSearches(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Supprimer une recherche sauvegardée")
    public ResponseEntity<Void> deleteSearch(@PathVariable Long id, Authentication authentication) {
        rechercheSauvegardeService.deleteSearch(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
