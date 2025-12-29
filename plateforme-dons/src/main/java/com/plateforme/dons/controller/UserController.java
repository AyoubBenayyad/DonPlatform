package com.plateforme.dons.controller;

import com.plateforme.dons.entity.User;
import com.plateforme.dons.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PatchMapping("/preferences/notifications")
    public ResponseEntity<?> toggleNotifications(@RequestBody Map<String, Boolean> body) {
        Boolean active = body.get("active");
        if (active == null) {
            return ResponseEntity.badRequest().body("Le champ 'active' est requis.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setNotificationsActive(active);
        userRepository.save(user);

        return ResponseEntity.ok().body(
                Map.of("message", "Préférences mises à jour", "notificationsActive", user.isNotificationsActive()));
    }
}
