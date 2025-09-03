package co.com.bancolombia.utils;

import co.com.bancolombia.model.user.User;

public interface JwtService {
    String generateToken(User user);
    boolean validateToken(String token);
    String getRoleFromToken(String token);
}