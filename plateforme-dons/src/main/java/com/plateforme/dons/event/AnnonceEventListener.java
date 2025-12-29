package com.plateforme.dons.event;

import com.plateforme.dons.entity.Annonce;
import com.plateforme.dons.entity.RechercheSauvegarde;
import com.plateforme.dons.repository.RechercheSauvegardeRepository;
import com.plateforme.dons.service.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AnnonceEventListener {

    private final RechercheSauvegardeRepository searchRepository;
    private final NotificationService notificationService;
    private final com.plateforme.dons.service.EmailService emailService;

    @EventListener
    @Async // Run asynchronously to not block the user
    public void handleAnnonceCreated(AnnonceCreatedEvent event) {
        Annonce annonce = event.getAnnonce();
        List<RechercheSauvegarde> allSearches = searchRepository.findAll();

        for (RechercheSauvegarde search : allSearches) {
            if (search.getUser().getId().equals(annonce.getCreateur().getId())) {
                continue;
            }

            if (!search.getUser().isNotificationsActive()) {
                continue;
            }

            if (matches(search, annonce)) {
                String message = "Nouveauté : Une annonce correspond à votre recherche '" + search.getTitre() + "'";
                String link = "/annonces/" + annonce.getId();
                notificationService.createNotification(search.getUser(), message, link);

                // Send Email Notification
                String emailSubject = "Nouvelle annonce : " + annonce.getTitre();
                String emailContent = "Bonjour " + search.getUser().getUsername() + ",\n\n" +
                        "Une nouvelle annonce correspond à votre recherche '" + search.getTitre() + "' : \n\n" +
                        "Titre: " + annonce.getTitre() + "\n" +
                        "Description: " + annonce.getDescription() + "\n\n" +
                        "Voir l'annonce: http://localhost:8080/annonces/" + annonce.getId() + "\n\n" +
                        "L'équipe Plateforme-Dons";

                emailService.sendNotificationEmail(search.getUser().getEmail(), emailSubject, emailContent);
            }
        }
    }

    private boolean matches(RechercheSauvegarde search, Annonce annonce) {
        // Keyword Match (Containment, Case In-sensitive)
        if (search.getMotCle() != null && !search.getMotCle().isBlank()) {
            String searchLower = search.getMotCle().toLowerCase();
            String titleLower = annonce.getTitre().toLowerCase();
            String descLower = annonce.getDescription().toLowerCase();

            if (!titleLower.contains(searchLower) && !descLower.contains(searchLower)) {
                return false;
            }
        }

        // Zone Match (Exact for now, or "contains")
        if (search.getZoneGeographique() != null && !search.getZoneGeographique().isBlank()) {
            if (annonce.getZoneGeographique() == null ||
                    !annonce.getZoneGeographique().equalsIgnoreCase(search.getZoneGeographique())) {
                return false;
            }
        }

        // State Match
        if (search.getEtat() != null) {
            if (annonce.getEtat() != search.getEtat()) {
                return false;
            }
        }

        return true;
    }
}
