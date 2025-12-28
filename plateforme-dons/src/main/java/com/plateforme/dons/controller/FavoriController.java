package com.plateforme.dons.controller;

import com.plateforme.dons.dto.response.AnnonceResponse;
import com.plateforme.dons.service.interfaces.FavoriService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favoris")
@RequiredArgsConstructor
public class FavoriController {

    private final FavoriService favoriService;

    @PostMapping("/{annonceId}")
    public ResponseEntity<Void> addFavori(@PathVariable Long annonceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        favoriService.addFavori(annonceId, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{annonceId}")
    public ResponseEntity<Void> removeFavori(@PathVariable Long annonceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        favoriService.removeFavori(annonceId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<AnnonceResponse>> getFavoris(@PageableDefault(size = 10) Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(favoriService.getFavoris(username, pageable));
    }
}
