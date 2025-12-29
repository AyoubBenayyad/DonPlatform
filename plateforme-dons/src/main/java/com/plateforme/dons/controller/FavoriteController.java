package com.plateforme.dons.controller;

import com.plateforme.dons.entity.Annonce;
import com.plateforme.dons.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/favoris")
    public String showFavorites(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        Set<Annonce> favorites = favoriteService.getFavorites(authentication.getName());
        model.addAttribute("favorites", favorites);
        return "user/favorites";
    }

    @PostMapping("/api/favorites/toggle/{annonceId}")
    @ResponseBody
    public ResponseEntity<String> toggleFavorite(@PathVariable Long annonceId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        favoriteService.toggleFavorite(authentication.getName(), annonceId);
        return ResponseEntity.ok("Toggled");
    }

    @GetMapping("/api/favorites/ids")
    @ResponseBody
    public ResponseEntity<Set<Long>> getFavoriteIds(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        Set<Annonce> favorites = favoriteService.getFavorites(authentication.getName());
        Set<Long> ids = favorites.stream().map(Annonce::getId).collect(Collectors.toSet());
        return ResponseEntity.ok(ids);
    }
}
