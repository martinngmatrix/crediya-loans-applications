package co.com.bancolombia.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.utils.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;


    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(expiry)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    @Override
    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.getBytes())).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}