package com.tbaruth.todogateway.security;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class SpaCsrfTokenRequestHandler extends ServerCsrfTokenRequestAttributeHandler {

  private final ServerCsrfTokenRequestHandler delegate = new XorServerCsrfTokenRequestAttributeHandler();

  @Override
  public void handle(ServerWebExchange exchange, Mono<CsrfToken> csrfToken) {
    this.delegate.handle(exchange, csrfToken);
  }

  @Override
  public Mono<String> resolveCsrfTokenValue(ServerWebExchange exchange, CsrfToken csrfToken) {
    /*
     * If the request contains a request header, use CsrfTokenRequestAttributeHandler
     * to resolve the CsrfToken. This applies when a single-page application includes
     * the header value automatically, which was obtained via a cookie containing the
     * raw CsrfToken.
     */
    if (exchange.getRequest().getHeaders().get(csrfToken.getHeaderName()) != null &&
        !exchange.getRequest().getHeaders().get(csrfToken.getHeaderName()).isEmpty() &&
        StringUtils.hasText(exchange.getRequest().getHeaders().get(csrfToken.getHeaderName()).getFirst())) {
      return super.resolveCsrfTokenValue(exchange, csrfToken);
    }
    /*
     * In all other cases (e.g. if the request contains a request parameter), use
     * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
     * when a server-side rendered form includes the _csrf request parameter as a
     * hidden input.
     */
    return this.delegate.resolveCsrfTokenValue(exchange, csrfToken);
  }
}
