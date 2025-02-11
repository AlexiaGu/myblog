package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final ArticleRepository articleRepository;

    public AuthorController(AuthorRepository authorRepository, ArticleAuthorRepository articleAuthorRepository, ArticleRepository articleRepository) {
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.articleRepository =articleRepository;
    }
    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstname(author.getFirstname());
        authorDTO.setLastname(author.getLastname());

        if(author.getArticleAuthors() != null) {
            authorDTO.setArticles(author.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getArticle() != null)
                    .map(articleAuthor -> {
                        ArticleDTO articleDTO = new ArticleDTO();
                        articleDTO.setId(articleAuthor.getArticle().getId());
                        articleDTO.setTitle(articleAuthor.getArticle().getTitle());
                        articleDTO.setContent(articleAuthor.getArticle().getContent());
                        articleDTO.setUpdatedAt(articleAuthor.getArticle().getUpdatedAt());
                        articleDTO.setCategoryName(articleAuthor.getArticle().getCategory().getName());

                        if (articleAuthor.getArticle().getImages() != null) {
                            articleDTO.setImageUrls(articleAuthor.getArticle().getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
                        }

                        return articleDTO;
                    }).collect(Collectors.toList()));
        }
        return authorDTO;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthor() {
        List<Author> authors = authorRepository.findAll();
        if(authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<AuthorDTO> authorsDTO = authors.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(authorsDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(author));
    }

    // Post
    @PostMapping
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody Author author) {
        author.setFirstname(author.getFirstname());
        author.setLastname(author.getLastname());

        Author savedAuthor = authorRepository.save(author);

        if(author.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : author.getArticleAuthors()) {
                Article article = articleAuthor.getArticle();
                article =articleRepository.findById(article.getId()).orElse(null);
                if(article == null) {
                    return ResponseEntity.badRequest().body(null);
                }

                articleAuthor.setArticle(article);
                articleAuthor.setAuthor(savedAuthor);
                articleAuthor.setContribution(articleAuthor.getContribution());

                articleAuthorRepository.save(articleAuthor);
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedAuthor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            return ResponseEntity.notFound().build();
        }

        author.setFirstname(author.getFirstname());
        author.setLastname(author.getLastname());

        if(authorDetails.getArticleAuthors() != null) {
            for (ArticleAuthor oldarticleAuthor : authorDetails.getArticleAuthors()) {
                articleAuthorRepository.delete(oldarticleAuthor);
            }

            List<ArticleAuthor> updateArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : authorDetails.getArticleAuthors()) {
                Article article =  articleAuthorDetails.getArticle();
                article =articleRepository.findById(article.getId()).orElse(null);

                if(article == null) {
                    return ResponseEntity.badRequest().body(null);
                }

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updateArticleAuthors.add(newArticleAuthor);

                for (ArticleAuthor articleAuthor : updateArticleAuthors) {
                    articleAuthorRepository.save(articleAuthor);
                }

                article.setArticleAuthors(updateArticleAuthors);
            }
        }

        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(convertToDTO(savedAuthor));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {

        Author author = authorRepository.findById(id).orElse(null);
        if(author == null) {
            return ResponseEntity.notFound().build();
        }

        if(author.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : author.getArticleAuthors()) {
                articleAuthorRepository.delete(articleAuthor);
            }
        }

        authorRepository.delete(author);
        return ResponseEntity.noContent().build();
    }
}