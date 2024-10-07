package com.tbaruth.todocore.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tbaruth.todocore.dto.UserDto;
import com.tbaruth.todocore.entity.User;
import com.tbaruth.todocore.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
  private final UserRepo userRepo;
  private final ExecutorService genExecutor;
  private final Cache<String, Long> usernameIdCache;

  public UserService(UserRepo userRepo, ExecutorService genExecutor, Cache<String, Long> usernameIdCache) {
    this.userRepo = userRepo;
    this.genExecutor = genExecutor;
    this.usernameIdCache = usernameIdCache;
  }

  public Future<UserDto> getOrCreateUser(Authentication authentication) {
    return genExecutor.submit(() -> {
      if (authentication.getPrincipal() instanceof Jwt jwt) {
        User user = userRepo.findByUsername(jwt.getClaim("preferred_username"));
        if (user == null) {
          user = new User();
          user.setUsername(jwt.getClaim("preferred_username"));
          user.setFirstName(jwt.getClaim("given_name"));
          user.setLastName(jwt.getClaim("family_name"));
          user.setEmail(jwt.getClaim("email"));
          user.setDarkMode(false);
          user = userRepo.save(user);
        }
        usernameIdCache.put(user.getUsername(), user.getId());
        return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isDarkMode());
      }
      return null;
    });
  }

  public Long getCurrentUserId() {
    return getUserId(SecurityContextHolder.getContext().getAuthentication());
  }

  public Long getUserId(Authentication authentication) {
    if (authentication.getPrincipal() instanceof Jwt jwt) {
      String username = jwt.getClaim("preferred_username");
      Long id = usernameIdCache.getIfPresent(username);
      if (id == null) {
        try {
          UserDto userDto = getOrCreateUser(authentication).get();
          if (userDto != null) {
            return userDto.id();
          }
        } catch (InterruptedException | ExecutionException e) {
          LOG.error("Could not populate user {} into cache", username, e);
        }
      } else {
        return id;
      }
    }
    return null;
  }

  public Future<UserDto> toggleDarkMode(Long userId, boolean darkMode) {
    return genExecutor.submit(() -> {
      User user = userRepo.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
      user.setDarkMode(darkMode);
      user = userRepo.save(user);
      return new UserDto(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isDarkMode());
    });
  }
}
