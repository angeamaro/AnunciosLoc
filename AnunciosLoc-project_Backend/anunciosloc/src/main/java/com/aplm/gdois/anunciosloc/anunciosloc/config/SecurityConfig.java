package com.aplm.gdois.anunciosloc.anunciosloc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Desativa CSRF para APIs REST
            .authorizeHttpRequests(auth -> auth
                // Libera autenticação e CRUD de locations
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/locations/**").permitAll()
                .requestMatchers("/api/messages/**").permitAll()
                .requestMatchers("/api/profile-keys/**").permitAll()

                
                // Todas as outras rotas precisam de autenticação
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
