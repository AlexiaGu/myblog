package org.wildcodeschool.myblog.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(article);
    }

    @GetMapping("/search-title")
    public ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms) {
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

//    méthode qui retourne une liste d'articles dont le contenu contient une chaine de caractère fournie en paramètre,
    @GetMapping("/search-content")
    public ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String keywords) {
        List<Article> articles = articleRepository.findByContentContaining(keywords);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }
// méthode qui retourne une liste d'articles créée après une date fournie en paramètre
    @GetMapping("/search-date")
    public ResponseEntity<List<Article>> getArticlesCreateAfter(@RequestParam LocalDateTime searchDate) {
            List<Article> articles = articleRepository.findByCreatedAtAfter(searchDate);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    // une méthode qui retourne les 5 derniers articles créés et classés du plus récent au plus ancien.
    @GetMapping("/search-last-article")
    public ResponseEntity<List<Article>> getFiveLastArticles() {
        List<Article> articles = articleRepository.findTopFiveByOrderByCreatedAtDesc();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());
        Article updatedArticle = articleRepository.save(article);
        return ResponseEntity.ok(updatedArticle);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }

        articleRepository.delete(article);
        return ResponseEntity.noContent().build();
    }


}