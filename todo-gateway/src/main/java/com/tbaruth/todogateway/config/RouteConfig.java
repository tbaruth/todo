package com.tbaruth.todogateway.config;

import com.tbaruth.todogateway.security.BeforeFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.removeRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.stripPrefix;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class RouteConfig {

  @Bean
  public RouterFunction<ServerResponse> getRoute() {
    return route("todos-core")
        //Cookies are not needed by the downstream pods, only the jwt is
        .before(removeRequestHeader("Cookie"))
        //.before(stripPrefix(2))
        .route(path("/api/todo-lists**"), http("http://localhost:8081"))
        //Add the JWT to the request
        .before(BeforeFilterFunctions.addBearerAuth())
        /*.filter(circuitBreaker(config -> config.setId("circuitBreaker")
            .setFallbackUri("forward:/err413")
            .setStatusCodes("413", "500")))*/
        .build()
        .and(route("error-fallback")
            .route(path("/err413"), http("http://localhost:8090"))
            .build());
  }
}
