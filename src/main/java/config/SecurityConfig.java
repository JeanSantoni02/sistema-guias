package com.transportista.sistemaguias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/h2-console/**").permitAll()
                // CORREGIDO: Usar {id} en lugar de **
                .requestMatchers("/api/guias/{id}/descargar").hasRole("DESCARGAR")
                .anyRequest().hasRole("ADMIN")
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String issuerUri = "https://login.microsoftonline.com/2f296fb5-af38-4a42-ac9c-f29fcaeb8f96/v2.0";
        return NimbusJwtDecoder.withIssuerLocation(issuerUri).build();
    }
}