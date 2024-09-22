package com.tbaruth.todogateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

  //TODO tbaruth Consider:  might want to create some dev-only jwts and have a "dev" config and a "test/prod" config--we only want two users (a regular user and an admin user)
  // Dev--MAYBE set up a docker image?  This slows down dev though...perhaps include a "simple-auth" profile option where we skip Keycloak
  // Test/prod config might use a keycloak docker image (we'll see)
  @Bean
  public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(req -> req
            .requestMatchers("/api").permitAll()
            .anyRequest().authenticated())
        .exceptionHandling(e -> e
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        )
        .oauth2Login(l -> {})
        .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/").permitAll());
    return http.build();
  }
}
