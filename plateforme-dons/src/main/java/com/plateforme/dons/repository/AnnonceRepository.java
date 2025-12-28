package com.plateforme.dons.repository;

import com.plateforme.dons.entity.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long>, JpaSpecificationExecutor<Annonce> {

    @org.springframework.data.jpa.repository.Query(value = "SELECT DISTINCT titre FROM annonces WHERE LOWER(titre) LIKE LOWER(CONCAT('%', :query, '%')) UNION SELECT DISTINCT mots_cles FROM annonce_mots_cles WHERE LOWER(mots_cles) LIKE LOWER(CONCAT('%', :query, '%')) LIMIT 10", nativeQuery = true)
    java.util.List<String> findSuggestions(@org.springframework.data.repository.query.Param("query") String query);
}
