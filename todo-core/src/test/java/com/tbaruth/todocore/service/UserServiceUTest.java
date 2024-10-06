package com.tbaruth.todocore.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.tbaruth.todocore.dto.UserDto;
import com.tbaruth.todocore.entity.User;
import com.tbaruth.todocore.repo.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.security.Principal;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class UserServiceUTest {

  private UserRepo userRepo;
  private ExecutorService genExecutor;
  private Cache<String, Long> usernameIdCache;

  private UserService service;

  @BeforeEach
  void setUp() {
    userRepo = mock(UserRepo.class);
    genExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("GenExec-", 0).factory());
    usernameIdCache = mock(Cache.class);

    service = new UserService(userRepo, genExecutor, usernameIdCache);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(userRepo, usernameIdCache);
  }

  @Nested
  @DisplayName("getOrCreateUser")
  class GetOrCreateUserTests {

    private Authentication auth;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
      Jwt jwt = mock(Jwt.class);
      when(jwt.getClaim("preferred_username")).thenReturn("bob");
      when(jwt.getClaim("given_name")).thenReturn("Bob");
      when(jwt.getClaim("family_name")).thenReturn("Smith");
      when(jwt.getClaim("email")).thenReturn("bob@test.com");
      auth = mock(Authentication.class);
      when(auth.getPrincipal()).thenReturn(jwt);
      user = mock(User.class);
      when(user.getId()).thenReturn(1L);
      when(user.getUsername()).thenReturn("bob");
      when(user.getFirstName()).thenReturn("Bob");
      when(user.getLastName()).thenReturn("Smith");
      when(user.getEmail()).thenReturn("bob@test.com");
      when(user.isDarkMode()).thenReturn(true);
      userDto = new UserDto(1L, "bob", "Bob", "Smith", "bob@test.com", true);
    }

    @Test
    void nonJwtAuthenticationReturnsNull() throws Exception {
      Authentication auth = mock(Authentication.class);
      when(auth.getPrincipal()).thenReturn(mock(Principal.class));

      assertNull(service.getOrCreateUser(auth).get());
    }

    @Test
    void foundUserGetsReturned() throws Exception {
      when(userRepo.findByUsername("bob")).thenReturn(user);

      assertEquals(userDto, service.getOrCreateUser(auth).get());

      verify(userRepo).findByUsername("bob");
      verify(usernameIdCache).put("bob", 1L);
    }

    @Test
    void unfoundUserGetsCreated() throws Exception {
      User newUser = new User();
      newUser.setUsername("bob");
      newUser.setFirstName("Bob");
      newUser.setLastName("Smith");
      newUser.setEmail("bob@test.com");
      newUser.setDarkMode(false);

      when(userRepo.save(newUser)).thenReturn(user);

      assertEquals(userDto, service.getOrCreateUser(auth).get());

      verify(userRepo).findByUsername("bob");
      verify(userRepo).save(newUser);
      verify(usernameIdCache).put("bob", 1L);
    }
  }

  @Test
  void getCurrentUserId() {
    UserService spy = spy(service);

    Authentication auth = mock(Authentication.class);
    SecurityContextHolder.getContext().setAuthentication(auth);

    doReturn(1L).when(spy).getUserId(auth);

    assertEquals(1L, spy.getCurrentUserId());

    verify(spy).getCurrentUserId();
    verify(spy).getUserId(auth);
    verifyNoMoreInteractions(spy);
  }

  @Nested
  @DisplayName("getUserId")
  class GetUserIdTests {

    private Authentication auth;

    @BeforeEach
    void setUp() {
      Jwt jwt = mock(Jwt.class);
      when(jwt.getClaim("preferred_username")).thenReturn("bob");
      auth = mock(Authentication.class);
      when(auth.getPrincipal()).thenReturn(jwt);
    }

    @Test
    void nonJwtAuthenticationReturnsNull() {
      Authentication auth = mock(Authentication.class);
      when(auth.getPrincipal()).thenReturn(mock(Principal.class));

      assertNull(service.getUserId(auth));
    }

    @Test
    void idFound() {
      when(usernameIdCache.getIfPresent("bob")).thenReturn(1L);

      assertEquals(1L, service.getUserId(auth));

      verify(usernameIdCache).getIfPresent("bob");
    }

    @Test
    void idNotFound_userCreated() {
      UserService spy = spy(service);

      UserDto userDto = mock(UserDto.class);
      when(userDto.id()).thenReturn(1L);

      doReturn(CompletableFuture.completedFuture(userDto)).when(spy).getOrCreateUser(auth);

      assertEquals(1L, spy.getUserId(auth));

      verify(spy).getUserId(auth);
      verify(usernameIdCache).getIfPresent("bob");
      verify(spy).getOrCreateUser(auth);
      verifyNoMoreInteractions(spy);
    }

    @Test
    void idNotFound_userNotCreated() {
      UserService spy = spy(service);

      doReturn(CompletableFuture.completedFuture(null)).when(spy).getOrCreateUser(auth);

      assertNull(spy.getUserId(auth));

      verify(spy).getUserId(auth);
      verify(usernameIdCache).getIfPresent("bob");
      verify(spy).getOrCreateUser(auth);
      verifyNoMoreInteractions(spy);
    }
  }

  @Test
  void toggleDarkMode() throws Exception {
    User user = mock(User.class);
    User savedUser = mock(User.class);
    when(savedUser.getId()).thenReturn(1L);
    when(savedUser.getUsername()).thenReturn("bob");
    when(savedUser.getFirstName()).thenReturn("Bob");
    when(savedUser.getLastName()).thenReturn("Smith");
    when(savedUser.getEmail()).thenReturn("bob@test.com");
    when(savedUser.isDarkMode()).thenReturn(true);
    boolean darkMode = new Random().nextBoolean();
    UserDto userDto = new UserDto(1L, "bob", "Bob", "Smith", "bob@test.com", true);

    when(userRepo.findById(1L)).thenReturn(Optional.of(user));
    when(userRepo.save(user)).thenReturn(savedUser);

    assertEquals(userDto, service.toggleDarkMode(1L, darkMode).get());

    verify(userRepo).findById(1L);
    verify(user).setDarkMode(darkMode);
    verify(userRepo).save(user);
    verifyNoMoreInteractions(user);
  }
}
