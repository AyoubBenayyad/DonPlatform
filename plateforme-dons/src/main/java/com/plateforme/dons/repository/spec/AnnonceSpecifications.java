package com.plateforme.dons.repository.spec;

import com.plateforme.dons.dto.payload.RechercheAnnonceCriteria;
import com.plateforme.dons.entity.Annonce;
import com.plateforme.dons.entity.StatutAnnonce;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AnnonceSpecifications {

    public static Specification<Annonce> withCriteria(RechercheAnnonceCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Toujours filtrer sur les annonces DISPONIBLE par défaut ? Ou inclus dans la
            // recherche
            // Pour l'instant on ne retourne que les disponibles pour les recherches
            // publiques
            predicates.add(criteriaBuilder.equal(root.get("statut"), StatutAnnonce.DISPONIBLE));

            if (StringUtils.hasText(criteria.getMotCle())) {
                String pattern = "%" + criteria.getMotCle().toLowerCase() + "%";
                Predicate titreMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("titre")), pattern);
                Predicate descMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);

                // Join motsCles
                jakarta.persistence.criteria.Join<Annonce, String> motsClesJoin = root.join("motsCles",
                        jakarta.persistence.criteria.JoinType.LEFT);
                Predicate tagsMatch = criteriaBuilder.like(criteriaBuilder.lower(motsClesJoin), pattern);

                // Combine: (Title OR Desc OR Tag) AND Status
                // Distinct is needed when joining to avoid duplicate rows
                query.distinct(true);

                predicates.add(criteriaBuilder.or(titreMatch, descMatch, tagsMatch));
            }

            if (StringUtils.hasText(criteria.getZoneGeographique())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("zoneGeographique")),
                        "%" + criteria.getZoneGeographique().toLowerCase() + "%"));
            }

            if (criteria.getEtat() != null) {
                predicates.add(criteriaBuilder.equal(root.get("etat"), criteria.getEtat()));
            }

            if (criteria.getModeRemise() != null) {
                predicates.add(criteriaBuilder.equal(root.get("modeRemise"), criteria.getModeRemise()));
            }

            if (criteria.getDateMin() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("datePublication"),
                        criteria.getDateMin().atStartOfDay()));
            }

            if (criteria.getDateMax() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("datePublication"),
                        criteria.getDateMax().atTime(23, 59, 59)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
