package org.wildcodeschool.myblog.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public class ArticleCreateDTO {
    @NotBlank(message = "Le titre ne doit pas être vide")
    @Size(min = 2, max = 50, message = "Le titre doit contenir entre 2 et 50 caractères")
    private String titre;

    @NotBlank(message = "Le contenu ne doit pas être vide")
    @Size(min = 10, message = "Le contenu doit contenir au moins 10 caractères")
    private String content;

    @NotNull(message = "L'id de la catégorie ne doit pas être nul")
    @Positive(message = "L'id de la catégorie doit être un nombre positif")
    private Long categoryId;

    @NotEmpty(message = "La liste des images ne doit pas être vide")
    private List<@Valid ImageDTO> images;

    @NotEmpty(message = "La liste des auteurs ne doit pas être vide")
    private List<@Valid AuthorContributionDTO> authors;

    // Getters et setters

    public String getTitle() {
        return titre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setTitle(String titre) {
        this.titre = titre;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public List<AuthorContributionDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorContributionDTO> authors) {
        this.authors = authors;
    }
}
