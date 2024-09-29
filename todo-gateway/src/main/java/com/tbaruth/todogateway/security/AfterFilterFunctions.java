package com.tbaruth.todogateway.security;

import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.BiFunction;

public class AfterFilterFunctions {

  public static BiFunction<ServerRequest, ServerResponse, ServerResponse> instrument(String header) {
    return (request, response) -> {
      response.headers().add(header, "123");
      return response;
    };
  }
}
