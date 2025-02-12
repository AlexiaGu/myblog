package org.wildcodeschool.myblog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(
            AuthorRepository authorRepository,
            ArticleAuthorRepository articleAuthorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        if(authors.isEmpty()) {
            throw new ResourceNotFoundException("Aucun auteur n'a été trouvé.");
        }
        return authors.stream().map(authorMapper::convertToDTO).collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'auteur correspondant à votre recherche est introuvable."));;
        return authorMapper.convertToDTO(author);
    }

    public AuthorDTO createAuthor(Author author) {
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(Long id, Author authorDetails) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'auteur que vous souhaitez mettre à jour est introuvable."));
        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.convertToDTO(updatedAuthor);
    }

    public boolean deleteAuthor(Long id) {
        Author author = authorRepository.findById(id).orElse(null);
        if (author == null) {
            throw new ResourceNotFoundException("L'auteur que vous souhaitez supprimer n'a pas été trouvé.");
        }

        articleAuthorRepository.deleteAll(author.getArticleAuthors());
        authorRepository.delete(author);
        return true;
    }
}
