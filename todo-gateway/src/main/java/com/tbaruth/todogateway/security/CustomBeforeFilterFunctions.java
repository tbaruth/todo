package com.tbaruth.todogateway.security;

//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

public class CustomBeforeFilterFunctions {

//  public static Function<ServerRequest, ServerRequest> addBearerAuth() {
//    return (request) -> {
//      SecurityContextHolder.getContext().getAuthentication();
//      OidcUser user = (OidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//      String idToken = user.getIdToken().getTokenValue();
//      return ServerRequest.from(request).header("Authorization", "Bearer " + idToken).build();
//    };
//  }
}
