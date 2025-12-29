package com.plateforme.dons.service;

import com.plateforme.dons.entity.Annonce;
import com.plateforme.dons.entity.User;
import com.plateforme.dons.repository.AnnonceRepository;
import com.plateforme.dons.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final UserRepository userRepository;
    private final AnnonceRepository annonceRepository;

    public void toggleFavorite(String username, Long annonceId) {
        log.info("Toggling favorite for user: {} and annonce: {}", username, annonceId);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new RuntimeException("Annonce not found"));

        if (user.getFavoris().contains(annonce)) {
            user.getFavoris().remove(annonce);
            log.info("Removed favorite");
        } else {
            user.getFavoris().add(annonce);
            log.info("Added favorite");
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Set<Annonce> getFavorites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavoris();
    }
}
