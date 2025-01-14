package org.wildcodeschool.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.myblog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
 List<Article> findByTitle(String title);

// méthode qui retourne une liste d'articles dont le contenu contient une chaine de caractère fournie en paramètre,
 List<Article> findByContentContaining(String keyword);

 // méthode qui retourne une liste d'articles créée après une date fournie en paramètre,
 List<Article> findByCreatedAtAfter(LocalDateTime createdAt);

 // méthode qui retourne les 5 derniers articles créés et classés du plus récent au plus ancien.
 List<Article> findTopFiveByOrderByCreatedAtDesc();
}
