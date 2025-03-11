package org.wildcodeschool.myblog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // méthode génère un JWT pour un utilisateur authentifié.
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Définit le sujet du token (souvent le nom d'utilisateur)
                .claim("roles", userDetails.getAuthorities()) // Ajoute les rôles de l'utilisateur dans le token.
                .setIssuedAt(new Date()) // Définit la date de création du token.
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Définit la date d'expiration du token.
                .signWith(SignatureAlgorithm.HS256, secretKey) // Signe le token avec l'algorithme HS256 et la clé secrète.
                // Si besoin d'une solution performante et que l'app n'exige pas une sécurité maximale
                // .signWith(SignatureAlgorithm.HS512, secretKey) si besoin de renforcer la sécurité dans un contexte
                // sensible avec risque d'attaque contre le hachage.
                .compact();
    }

    // extrait les informations intégrées dans le JWT
    // Claims = revendications
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // Configure la clé secrète utilisée pour vérifier la signature du token.
                .parseClaimsJws(token) // Analyse le token JWT pour extraire les informations.
                .getBody(); // Récupère les revendications (données) du corps du token.
    }

    // Pour vérifier la validité et non expiration du token
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true; // Tente de parser le token en utilisant la clé secrète. Si le token est valide, cette opération réussit.
        } catch (JwtException | IllegalArgumentException e) {
            return false; // sinon une exception est retournée alors le token est soit invalide ou expiré
        }
    }

}
