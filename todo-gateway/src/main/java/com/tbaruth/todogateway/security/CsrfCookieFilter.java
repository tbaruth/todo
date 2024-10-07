package com.tbaruth.todogateway.security;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public final class CsrfCookieFilter implements WebFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return ((Mono<CsrfToken>)exchange.getAttribute("org.springframework.security.web.server.csrf.CsrfToken"))
        // Render the token value to a cookie by causing the deferred token to be loaded
        .map(csrfToken -> csrfToken.getToken())
        .flatMap(value -> chain.filter(exchange));
  }
}
