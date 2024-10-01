package com.tbaruth.todogateway.routes;

import com.tbaruth.todogateway.security.CustomAfterFilterFunctions;
/*import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.removeRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;*/

public class FrontendRouter {

  /*public static RouterFunction<ServerResponse> router() {
    return route("frontend-router")
        //Don't send session to the front-end
        .before(removeRequestHeader("cookie"))
        //Need to close the connection for dev server
        .before(removeRequestHeader("upgrade-insecure-requests"))
        .before(removeRequestHeader("accept-language"))
        .before(removeRequestHeader("accept-encoding"))
        .before(removeRequestHeader("cache-control"))
        .before(removeRequestHeader("keep-alive"))
        .before(removeRequestHeader("connection"))
        .before(removeRequestHeader("sec-ch-ua"))
        .before(removeRequestHeader("sec-ch-ua-mobile"))
        .before(removeRequestHeader("sec-ch-ua-platform"))
        .before(removeRequestHeader("sec-fetch-dest"))
        .before(removeRequestHeader("sec-fetch-mode"))
        .before(removeRequestHeader("sec-fetch-site"))
        .before(removeRequestHeader("sec-fetch-user"))
        .before(removeRequestHeader("user-agent"))
        .before(setRequestHeader("Host", "localhost"))
        .before(addRequestHeader("Origin", "localhost"))
        .before(addRequestHeader("X-Forwarded-Host", "localhost"))
        .before(addRequestHeader("X-Forwarded-Server", "localhost"))
        .before(addRequestHeader("X-Forwarded-Proto", "http"))
        .before(addRequestHeader("X-Forwarded-For", "localhost:8080"))
        .before(addRequestHeader("Access-Control-Allow-Origin", "localhost:8080"))
        .before(setRequestHeader("connection", "keep-alive"))
        .before(setRequestHeader("keep-alive", "timeout=5"))
        .before(setRequestHeader("accept", "text/html"))
        .before(setPath("/vue-ui"))
//        .GET("**", http("https://localhost:8080"))
        .route(path("**"), http("http://localhost:4202"))
        .after(CustomAfterFilterFunctions.instrument("test"))
        .after(AfterFilterFunctions.dedupeResponseHeader("Access-Control-Allow-Credentials Access-Control-Allow-Origin"))
        //.GET(http("http://localhost:4000"))
        .build();
  }*/

  /*
  RestTemplate restTemplate = new RestTemplate();
String fooResourceUrl
    = "http://localhost:8080";
ResponseEntity<String> response
    = restTemplate.getForEntity(fooResourceUrl, String.class);
   */

  /*
  RestTemplate restTemplate = new RestTemplate();
  HttpHeaders headers = new HttpHeaders();
  headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

  HttpEntity<String> entity = new HttpEntity<>("body", headers);

  restTemplate.exchange("http://localhost:4000", HttpMethod.POST, entity, String.class);

RestTemplate restTemplate = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.set("Cookie", "JSESSIONID=66DB8A179FFBDBC678E2AFBDCA1AAFF8");

HttpEntity<String> entity = new HttpEntity<>("body", headers);

restTemplate.exchange("http://localhost:4000", HttpMethod.GET, entity, String.class);

   */
}
