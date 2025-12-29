package com.plateforme.dons.repository;

import com.plateforme.dons.entity.RechercheSauvegarde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechercheSauvegardeRepository extends JpaRepository<RechercheSauvegarde, Long> {
    List<RechercheSauvegarde> findByUserUsername(String username);
}
