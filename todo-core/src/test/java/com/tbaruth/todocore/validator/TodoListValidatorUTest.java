package com.tbaruth.todocore.validator;

import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.entity.User;
import com.tbaruth.todocore.service.TodoListService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TodoListValidatorUTest {

  private TodoListService todoListService;
  private ExecutorService genExecutor;
  private TodoListValidator validator;

  @BeforeEach
  void setUp() {
    todoListService = mock(TodoListService.class);
    genExecutor = new DelegatingSecurityContextExecutorService(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("GenExec-", 0).factory()));

    validator = new TodoListValidator(todoListService, genExecutor);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(todoListService);
  }

  @Nested
  @DisplayName("validateCreate")
  class ValidateCreateTests {

    private TodoListCreateDto dto;

    @BeforeEach
    void setUp() {
      dto = mock(TodoListCreateDto.class);
      when(dto.title()).thenReturn("asdf");
    }

    @Test
    void success() {
      assertTrue(validator.validateCreate(dto));
    }

    @Test
    void failureOnTitleLongerThan250Chars() {
      when(dto.title()).thenReturn(StringUtils.leftPad("X", 251));

      assertFalse(validator.validateCreate(dto));
    }

    @Test
    void failureOnBlankTitle() {
      when(dto.title()).thenReturn("");

      assertFalse(validator.validateCreate(dto));
    }
  }

  @Nested
  @DisplayName("validateUpdate")
  class ValidateUpdateTests {

    private TodoListUpdateDto dto;
    private TodoList list;
    private Authentication auth;
    private User user;

    @BeforeEach
    void setUp() {
      dto = mock(TodoListUpdateDto.class);
      when(dto.title()).thenReturn("asdf");
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      list = mock(TodoList.class);
      when(list.getUser()).thenReturn(user);
      auth = mock(Authentication.class);
      when(auth.getName()).thenReturn("bob");

      SecurityContextHolder.getContext().setAuthentication(auth);
      when(todoListService.getTodoList(1L)).thenReturn(CompletableFuture.completedFuture(list));
    }

    @Test
    void success() throws Exception {
      assertTrue(validator.validateUpdate(1L, dto).get());

      verify(todoListService).getTodoList(1L);
    }

    @Test
    void failureOnOwnership() throws Exception {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(validator.validateUpdate(1L, dto).get());

      verify(todoListService).getTodoList(1L);
    }

    @Test
    void failureOnNonexistentList() throws Exception {
      when(todoListService.getTodoList(1L)).thenReturn(CompletableFuture.completedFuture(null));

      assertFalse(validator.validateUpdate(1L, dto).get());

      verify(todoListService).getTodoList(1L);
    }

    @Test
    void failureOnTitleLongerThan250Chars() throws Exception {
      when(dto.title()).thenReturn(StringUtils.leftPad("X", 251));

      assertFalse(validator.validateUpdate(1L, dto).get());
    }

    @Test
    void failureOnBlankTitle() throws Exception {
      when(dto.title()).thenReturn("");

      assertFalse(validator.validateUpdate(1L, dto).get());
    }
  }

  @Nested
  @DisplayName("validateDelete")
  class ValidateDeleteTests {

    private TodoList list;
    private Authentication auth;
    private User user;

    @BeforeEach
    void setUp() {
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      list = mock(TodoList.class);
      when(list.getUser()).thenReturn(user);
      auth = mock(Authentication.class);
      when(auth.getName()).thenReturn("bob");

      SecurityContextHolder.getContext().setAuthentication(auth);
      when(todoListService.getTodoList(1L)).thenReturn(CompletableFuture.completedFuture(list));
    }

    @Test
    void success() throws Exception {
      assertTrue(validator.validateDelete(1L).get());

      verify(todoListService).getTodoList(1L);
    }

    @Test
    void failureOnOwnership() throws Exception {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(validator.validateDelete(1L).get());

      verify(todoListService).getTodoList(1L);
    }

    @Test
    void failureOnNonexistentList() throws Exception {
      when(todoListService.getTodoList(1L)).thenReturn(CompletableFuture.completedFuture(null));

      assertFalse(validator.validateDelete(1L).get());

      verify(todoListService).getTodoList(1L);
    }
  }
}
