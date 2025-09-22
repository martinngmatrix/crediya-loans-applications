package co.com.bancolombia.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.bancolombia.jwt.JwtServiceImpl;
import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.utils.JwtService;

@Configuration
public class JwtConfig {
    @Bean
    public JwtService jwtService(GenericManagerAsync secretManager,
                                 @Value("${aws.secretName}") String secretName,
                                 @Value("${jwt.expiration}") long expiration) {
        return new JwtServiceImpl(secretManager, secretName, expiration);
    }
}
