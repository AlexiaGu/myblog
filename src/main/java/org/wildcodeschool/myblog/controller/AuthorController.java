package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.service.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // GET
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthor() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        if(authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        if(author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    // Post
    @PostMapping
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody Author author) {

        AuthorDTO savedAuthor = authorService.createAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetails) {
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDetails);
        if(updatedAuthor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAuthor);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        if(authorService.deleteAuthor(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}