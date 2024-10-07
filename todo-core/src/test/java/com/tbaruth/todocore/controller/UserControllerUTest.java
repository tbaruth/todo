package com.tbaruth.todocore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbaruth.todocore.config.ExecutorConfig;
import com.tbaruth.todocore.config.SecurityConfig;
import com.tbaruth.todocore.dto.UserDto;
import com.tbaruth.todocore.dto.incoming.DarkModeDto;
import com.tbaruth.todocore.security.SecurityService;
import com.tbaruth.todocore.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UserController.class, ExecutorConfig.class, SecurityConfig.class})
@WithMockUser
@EnableMethodSecurity
public class UserControllerUTest {

  @MockBean
  private UserService userService;
  @MockBean(name = "securityService")
  private SecurityService securityService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private ExecutorService executorService;
  @Autowired
  private MockMvc mockMvc;
  private Authentication auth;

  @BeforeEach
  void setUp() {
    auth = SecurityContextHolder.getContext().getAuthentication();
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(userService, securityService);
  }

  @Nested
  @DisplayName("getSelf")
  class GetSelfTests {

    @Test
    void successShouldReturnAUserDto() throws Exception {
      UserDto dto = new UserDto(1L, null, null, null, null, true);

      when(userService.getOrCreateUser(auth)).thenReturn(CompletableFuture.supplyAsync(() -> dto));

      MvcResult result = mockMvc.perform(get("/users")
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(dto)));

      verify(userService).getOrCreateUser(auth);
    }

    @Test
    void failureShouldReturnInternalServerError() throws Exception {
      @SuppressWarnings("unchecked")
      CompletableFuture<UserDto> future = mock(CompletableFuture.class);
      when(future.get()).thenThrow(new InterruptedException("expected"));

      when(userService.getOrCreateUser(auth)).thenReturn(future);

      MvcResult result = mockMvc.perform(get("/users")
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isInternalServerError());

      verify(userService).getOrCreateUser(auth);
    }
  }

  @Nested
  @DisplayName("toggleDarkMode")
  class ToggleDarkModeTests {

    @BeforeEach
    void setUp() {
      when(userService.getCurrentUserId()).thenReturn(1L);
    }

    @Test
    void successShouldReturnUserDto() throws Exception {
      DarkModeDto dto = new DarkModeDto(true);
      UserDto returnDto = new UserDto(5L, "nm", null, null, null, true);

      when(securityService.isAbleToToggleDarkMode(auth)).thenReturn(true);
      when(userService.toggleDarkMode(1L, true)).thenReturn(CompletableFuture.supplyAsync(() -> returnDto));

      MvcResult result = mockMvc.perform(put("/users")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isAccepted())
          .andExpect(content().json(objectMapper.writeValueAsString(returnDto)));

      verify(securityService).isAbleToToggleDarkMode(auth);
      verify(userService).getCurrentUserId();
      verify(userService).toggleDarkMode(1L, true);
    }

    @Test
    void failureShouldReturnInternalServerError() throws Exception {
      DarkModeDto dto = new DarkModeDto(true);

      @SuppressWarnings("unchecked")
      CompletableFuture<UserDto> future = mock(CompletableFuture.class);
      when(future.get()).thenThrow(new InterruptedException("expected"));

      when(securityService.isAbleToToggleDarkMode(auth)).thenReturn(true);
      when(userService.toggleDarkMode(1L, true)).thenReturn(future);

      MvcResult result = mockMvc.perform(put("/users")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isInternalServerError());

      verify(securityService).isAbleToToggleDarkMode(auth);
      verify(userService, times(2)).getCurrentUserId();
      verify(userService).toggleDarkMode(1L, true);
    }

    @Test
    void notAuthorizedShouldFail() throws Exception {
      DarkModeDto dto = new DarkModeDto(true);

      mockMvc.perform(put("/users")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andExpect(status().isForbidden());

      verify(securityService).isAbleToToggleDarkMode(auth);
    }
  }
}
