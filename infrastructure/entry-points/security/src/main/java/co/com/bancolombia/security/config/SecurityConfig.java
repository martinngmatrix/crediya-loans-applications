package co.com.bancolombia.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.bancolombia.model.user.gateways.JwtService;
import co.com.bancolombia.security.JwtAuthenticationFilter;
import co.com.bancolombia.security.RoleAuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public RoleAuthorizationFilter roleAuthorizationFilter() {
        return new RoleAuthorizationFilter();
    }
}