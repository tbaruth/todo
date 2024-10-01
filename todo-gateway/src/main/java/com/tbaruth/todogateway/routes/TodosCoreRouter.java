package com.tbaruth.todogateway.routes;

import com.tbaruth.todogateway.security.CustomBeforeFilterFunctions;
//import org.springframework.web.servlet.function.RouterFunction;
//import org.springframework.web.servlet.function.ServerResponse;
//
//import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.removeRequestHeader;
//import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
//import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
//import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

public class TodosCoreRouter {

//  public static RouterFunction<ServerResponse> router() {
//    return route("todos-core")
//        //Cookies are not needed by the downstream pods, only the jwt is
//        .before(removeRequestHeader("cookie"))
//        //.before(stripPrefix(2))
//        .route(path("/api/todo-lists**"), http("http://localhost:8081"))
//        //Add the JWT to the request
//        .before(CustomBeforeFilterFunctions.addBearerAuth())
//        /*.filter(circuitBreaker(config -> config.setId("circuitBreaker")
//            .setFallbackUri("forward:/err413")
//            .setStatusCodes("413", "500")))*/
//        .build();
//  }
}
