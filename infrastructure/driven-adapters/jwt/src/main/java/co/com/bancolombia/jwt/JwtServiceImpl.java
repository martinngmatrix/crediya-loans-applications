package co.com.bancolombia.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import co.com.bancolombia.jwt.model.SecretModel;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.bancolombia.utils.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

public class JwtServiceImpl implements JwtService {
    private final GenericManagerAsync secretManager;
    private final String secretName;
    private final long expiration;

    public JwtServiceImpl(GenericManagerAsync secretManager,
                          @Value("${aws.secretName}") String secretName,
                          @Value("${jwt.expiration}") long expiration) {
        this.secretManager = secretManager;
        this.secretName = secretName;
        this.expiration = expiration;
    }

    private Mono<String> getJwtSecret() {
        try {
            return secretManager.getSecret(secretName, SecretModel.class)
                    .map(SecretModel::getJWT_SECRET);
        } catch (SecretException e) {
            throw new RuntimeException("Error obteniendo credenciales de AWS Secrets Manager", e);
        }
    }

    @Override
    public Mono<String> generateToken(User user) {
        return getJwtSecret().map(secret -> {
            Date now = new Date();
            Date expiry = new Date(now.getTime() + expiration * 1000);

            return Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("role", user.getRole())
                    .setIssuedAt(now)
                    .setExpiration(expiry)
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .compact();
        });
    }

    @Override
    public Mono<String> getRoleFromToken(String token) {
        return getJwtSecret().map(secret ->
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class)
        );
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return getJwtSecret().map(secret -> {
            try {
                Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
}