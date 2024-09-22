package com.tbaruth.todocore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(req -> req
            .requestMatchers("/api/analytics", "/api/analytics/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers("/api/**").hasAuthority("ROLE_USER")
            .anyRequest().authenticated())
        //Disable csrf--this is stateless--only uses jwt
        .csrf(AbstractHttpConfigurer::disable)
        //No sessions behind the gateway
        .sessionManagement(sess -> sess
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .oauth2ResourceServer(server -> server.jwt(jwt -> {}));
    return http.build();
  }
}
