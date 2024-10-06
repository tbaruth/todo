package com.tbaruth.todocore.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

  /**
   * Create a basic cache for lookup of user ID from the passed-in JWT token.
   * Ideally, one might create a user in the gateway and then propagate it to this app via a message queue or web service.
   * This works well enough for messing around on dev projects.
   *
   * @return A caffeine cache for faster lookup of user ID from the jwt token
   */
  //TODO tbaruth 10-06-2024 consider situation where cache element is evicted and session still active--need to repopulate the cache somehow (currently just refresh the page, easy, but bad ux)
  @Bean
  public Cache<String, Long> usernameIdCache() {
    return Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(2L))
        .build();
  }
}
