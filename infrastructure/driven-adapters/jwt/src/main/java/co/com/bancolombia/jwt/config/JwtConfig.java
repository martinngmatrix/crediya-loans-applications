package co.com.bancolombia.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.bancolombia.jwt.JwtServiceImpl;
import co.com.bancolombia.utils.JwtService;

@Configuration
public class JwtConfig {
    @Bean
    public JwtService jwtService() {
        return new JwtServiceImpl();
    }
}
