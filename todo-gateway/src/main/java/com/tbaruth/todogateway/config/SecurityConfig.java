package com.tbaruth.todogateway.config;

import com.tbaruth.todogateway.security.AuthoritiesConverter;
import com.tbaruth.todogateway.security.CsrfCookieFilter;
import com.tbaruth.todogateway.security.SpaCsrfTokenRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.HTTP_BASIC;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

  @Bean
  AuthoritiesConverter realmRolesAuthoritiesConverter() {
    return claims -> {
      @SuppressWarnings("unchecked")
      var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
      @SuppressWarnings("unchecked")
      var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));
      return roles.stream().flatMap(Collection::stream)
          .map(SimpleGrantedAuthority::new)
          .map(GrantedAuthority.class::cast)
          .toList();
    };
  }

  @Bean
  GrantedAuthoritiesMapper authenticationConverter(AuthoritiesConverter authoritiesConverter) {
    return (authorities) -> authorities.stream()
        .filter(authority -> authority instanceof OidcUserAuthority)
        .map(OidcUserAuthority.class::cast)
        .map(OidcUserAuthority::getIdToken)
        .map(OidcIdToken::getClaims)
        .map(authoritiesConverter::convert)
        .flatMap(roles -> roles.stream())
        .collect(Collectors.toSet());
  }

  @Bean
  public SecurityWebFilterChain clientSecurityFilterChain(ServerHttpSecurity http) {
    http
        .authorizeExchange(req -> req
            .anyExchange().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .csrf(c -> c
            .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        )
        .addFilterAfter(new CsrfCookieFilter(), HTTP_BASIC);
        /*.logout(logout -> {
          var logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
          logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
          logout.logoutSuccessHandler(logoutSuccessHandler);
        })*/
    return http.build();
  }
}
