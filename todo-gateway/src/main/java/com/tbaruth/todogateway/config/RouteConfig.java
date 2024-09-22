package com.tbaruth.todogateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.removeRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.method;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class RouteConfig {

  @Bean
  public RouterFunction<ServerResponse> getRoute() {
    return route("todos-core")
        //Cookies are not needed by the downstream pods, only the jwt is
        .before(removeRequestHeader("Cookie"))
        //.before(stripPrefix(2))
        .route(
            path("/get")
                .and(method(HttpMethod.GET)),
            http("http://localhost:8080/api/todo-lists"))
        .filter(circuitBreaker(config -> config.setId("circuitBreaker")
            .setFallbackUri("forward:/err413")
            .setStatusCodes("413", "500")))
        .build()
        .and(route("error-fallback")
            .route(path("/err413"), http("http://localhost:8090"))
            .build());
  }
}
