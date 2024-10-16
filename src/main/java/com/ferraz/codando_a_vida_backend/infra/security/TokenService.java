package com.ferraz.codando_a_vida_backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ferraz.codando_a_vida_backend.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.issuer}")
    private String issuer;

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("perfil", "ROLE_USER")
                .withExpiresAt(generateExpirationDate())
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractSubject(String token) {
        var algorithm = Algorithm.HMAC256(secret);
        var verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        var decodedJWT = verifier.verify(token); // Valida o token
        return decodedJWT.getSubject(); // Pega o subject
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }

}