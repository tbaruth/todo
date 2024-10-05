package com.tbaruth.todocore.security;

import com.tbaruth.todocore.entity.TodoItem;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.entity.User;
import com.tbaruth.todocore.repo.TodoItemRepo;
import com.tbaruth.todocore.repo.TodoListRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SecurityServiceUTest {

  private TodoListRepo todoListRepo;
  private TodoItemRepo todoItemRepo;
  private Authentication auth;
  private SecurityService securityService;

  @BeforeEach
  void setUp() {
    todoListRepo = mock(TodoListRepo.class);
    todoItemRepo = mock(TodoItemRepo.class);
    auth = mock(Authentication.class);
    when(auth.getName()).thenReturn("bob");

    SecurityContextHolder.getContext().setAuthentication(auth);
    securityService = new SecurityService(todoListRepo, todoItemRepo);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(todoListRepo, todoItemRepo);
  }

  @Test
  void isAbleToViewTodoLists() {
    assertTrue(securityService.isAbleToViewTodoLists(auth));
  }

  @Test
  void isAbleToCreateTodoList() {
    assertTrue(securityService.isAbleToCreateTodoList(auth));
  }

  @Nested
  @DisplayName("isAbleToEditTodoList")
  class IsAbleToEditTodoListTests {

    private User user;
    private TodoList todoList;

    @BeforeEach
    void setUp() {
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      todoList = mock(TodoList.class);
      when(todoList.getUser()).thenReturn(user);

      when(todoListRepo.findById(1L)).thenReturn(Optional.of(todoList));
    }

    @AfterEach
    void tearDown() {
      verify(todoListRepo).findById(1L);
    }

    @Test
    void success() {
      assertTrue(securityService.isAbleToEditTodoList(1L, auth));
    }

    @Test
    void fail_notOwner() {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(securityService.isAbleToEditTodoList(1L, auth));
    }

    @Test
    void fail_notFound() {
      when(todoListRepo.findById(1L)).thenReturn(Optional.empty());

      assertFalse(securityService.isAbleToEditTodoList(1L, auth));
    }
  }

  @Nested
  @DisplayName("isAbleToViewTodos")
  class IsAbleToViewTodosTests {

    private User user;
    private TodoList todoList;

    @BeforeEach
    void setUp() {
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      todoList = mock(TodoList.class);
      when(todoList.getUser()).thenReturn(user);

      when(todoListRepo.findById(1L)).thenReturn(Optional.of(todoList));
    }

    @AfterEach
    void tearDown() {
      verify(todoListRepo).findById(1L);
    }

    @Test
    void success() {
      assertTrue(securityService.isAbleToViewTodos(1L, auth));
    }

    @Test
    void fail_notOwner() {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(securityService.isAbleToViewTodos(1L, auth));
    }

    @Test
    void fail_notFound() {
      when(todoListRepo.findById(1L)).thenReturn(Optional.empty());

      assertFalse(securityService.isAbleToViewTodos(1L, auth));
    }
  }

  @Nested
  @DisplayName("isAbleToCreateTodoItem")
  class IsAbleToCreateTodoItemTests {

    private User user;
    private TodoList todoList;

    @BeforeEach
    void setUp() {
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      todoList = mock(TodoList.class);
      when(todoList.getUser()).thenReturn(user);

      when(todoListRepo.findById(1L)).thenReturn(Optional.of(todoList));
    }

    @AfterEach
    void tearDown() {
      verify(todoListRepo).findById(1L);
    }

    @Test
    void success() {
      assertTrue(securityService.isAbleToCreateTodoItem(1L, auth));
    }

    @Test
    void fail_notOwner() {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(securityService.isAbleToCreateTodoItem(1L, auth));
    }

    @Test
    void fail_notFound() {
      when(todoListRepo.findById(1L)).thenReturn(Optional.empty());

      assertFalse(securityService.isAbleToCreateTodoItem(1L, auth));
    }
  }

  @Nested
  @DisplayName("isAbleToViewTodoItem")
  class IsAbleToViewTodoItemTests {

    private User user;
    private TodoList todoList;
    private TodoItem todoItem;

    @BeforeEach
    void setUp() {
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      todoList = mock(TodoList.class);
      when(todoList.getUser()).thenReturn(user);
      when(todoList.getId()).thenReturn(1L);
      todoItem = mock(TodoItem.class);
      when(todoItem.getList()).thenReturn(todoList);

      when(todoItemRepo.findById(2L)).thenReturn(Optional.of(todoItem));
    }

    @AfterEach
    void tearDown() {
      verify(todoItemRepo).findById(2L);
    }

    @Test
    void success() {
      assertTrue(securityService.isAbleToViewTodoItem(1L, 2L, auth));
    }

    @Test
    void fail_notOwner() {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(securityService.isAbleToViewTodoItem(1L, 2L, auth));
    }

    @Test
    void fail_wrongList() {
      when(todoList.getId()).thenReturn(10L);

      assertFalse(securityService.isAbleToViewTodoItem(1L, 2L, auth));
    }

    @Test
    void fail_notFound() {
      when(todoItemRepo.findById(2L)).thenReturn(Optional.empty());

      assertFalse(securityService.isAbleToViewTodoItem(1L, 2L, auth));
    }
  }

  @Nested
  @DisplayName("isAbleToEditTodoItem")
  class IsAbleToEditTodoItemTests {

    private User user;
    private TodoList todoList;
    private TodoItem todoItem;

    @BeforeEach
    void setUp() {
      user = mock(User.class);
      when(user.getEmail()).thenReturn("bob");
      todoList = mock(TodoList.class);
      when(todoList.getUser()).thenReturn(user);
      when(todoList.getId()).thenReturn(1L);
      todoItem = mock(TodoItem.class);
      when(todoItem.getList()).thenReturn(todoList);

      when(todoItemRepo.findById(2L)).thenReturn(Optional.of(todoItem));
    }

    @AfterEach
    void tearDown() {
      verify(todoItemRepo).findById(2L);
    }

    @Test
    void success() {
      assertTrue(securityService.isAbleToEditTodoItem(1L, 2L, auth));
    }

    @Test
    void fail_notOwner() {
      when(user.getEmail()).thenReturn("asdf");

      assertFalse(securityService.isAbleToEditTodoItem(1L, 2L, auth));
    }

    @Test
    void fail_wrongList() {
      when(todoList.getId()).thenReturn(10L);

      assertFalse(securityService.isAbleToEditTodoItem(1L, 2L, auth));
    }

    @Test
    void fail_notFound() {
      when(todoItemRepo.findById(2L)).thenReturn(Optional.empty());

      assertFalse(securityService.isAbleToEditTodoItem(1L, 2L, auth));
    }
  }
}
