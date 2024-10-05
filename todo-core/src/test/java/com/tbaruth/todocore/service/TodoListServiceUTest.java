package com.tbaruth.todocore.service;

import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.repo.TodoItemRepo;
import com.tbaruth.todocore.repo.TodoListRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TodoListServiceUTest {

  private TodoListRepo todoListRepo;
  private TodoItemRepo todoItemRepo;
  private ExecutorService genExecutor;
  private TodoListService service;

  @BeforeEach
  void setUp() {
    todoListRepo = mock(TodoListRepo.class);
    todoItemRepo = mock(TodoItemRepo.class);
    genExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("GenExec-", 0).factory());

    service = new TodoListService(todoListRepo, todoItemRepo, genExecutor);
  }

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(todoListRepo, todoItemRepo);
  }

  @Test
  void getTodoLists() throws Exception {
    LocalDateTime created = LocalDateTime.now().minusDays(2L);
    LocalDateTime updated = LocalDateTime.now().minusDays(1L);
    TodoList todoList = mock(TodoList.class);
    when(todoList.getId()).thenReturn(2L);
    when(todoList.getTitle()).thenReturn("nm");
    when(todoList.getCreated()).thenReturn(created);
    when(todoList.getUpdated()).thenReturn(updated);
    boolean completed = new Random().nextBoolean();
    TodoListDto expected = new TodoListDto(2L, "nm", created, updated, completed);

    when(todoListRepo.findByUserId(1L)).thenReturn(List.of(todoList));
    when(todoListRepo.isListCompleted(2L)).thenReturn(completed);

    List<Future<TodoListDto>> dtoFutures = service.getTodoLists(1L);
    assertEquals(1, dtoFutures.size());
    for (Future<TodoListDto> future : dtoFutures) {
      TodoListDto dto = future.get();
      assertEquals(expected, dto);
    }

    verify(todoListRepo).findByUserId(1L);
    verify(todoListRepo).isListCompleted(2L);
  }

  @Nested
  @DisplayName("getTodoList")
  class GetTodoListTests {
    @Test
    void listPresent() throws Exception {
      TodoList list = mock(TodoList.class);

      when(todoListRepo.findById(1L)).thenReturn(Optional.of(list));

      assertEquals(list, service.getTodoList(1L).get());

      verify(todoListRepo).findById(1L);
    }

    @Test
    void listNotPresent() throws Exception {
      when(todoListRepo.findById(1L)).thenReturn(Optional.empty());

      assertNull(service.getTodoList(1L).get());

      verify(todoListRepo).findById(1L);
    }
  }

  @Test
  void createTodoList() throws Exception {
    LocalDateTime before = LocalDateTime.now().minusNanos(1L);
    LocalDateTime created = LocalDateTime.now().minusDays(2L);
    LocalDateTime updated = LocalDateTime.now().minusDays(1L);
    TodoList savedList = mock(TodoList.class);
    when(savedList.getId()).thenReturn(1L);
    when(savedList.getTitle()).thenReturn("nm");
    when(savedList.getCreated()).thenReturn(created);
    when(savedList.getUpdated()).thenReturn(updated);

    when(todoListRepo.save(any(TodoList.class))).thenReturn(savedList);

    TodoListCreateDto createDto = mock(TodoListCreateDto.class);
    when(createDto.title()).thenReturn("nm");
    TodoListDto dto = new TodoListDto(1L, "nm", created, updated, true);

    assertEquals(dto, service.createTodoList(createDto).get());

    LocalDateTime after = LocalDateTime.now().plusNanos(1L);

    verify(todoListRepo).save(argThat(arg ->
        "nm".equals(arg.getTitle()) &&
        arg.getCreated().equals(arg.getUpdated()) &&
        arg.getCreated().isAfter(before) && arg.getCreated().isBefore(after) &&
        arg.getId() == null));
  }

  @Test
  void updateTodoList() throws Exception {
    LocalDateTime before = LocalDateTime.now().minusNanos(1L);
    LocalDateTime created = LocalDateTime.now().minusDays(2L);
    LocalDateTime updated = LocalDateTime.now().minusDays(1L);
    TodoList savedList = mock(TodoList.class);
    when(savedList.getId()).thenReturn(1L);
    when(savedList.getTitle()).thenReturn("nm");
    when(savedList.getCreated()).thenReturn(created);
    when(savedList.getUpdated()).thenReturn(updated);
    TodoList list = mock(TodoList.class);
    boolean completed = new Random().nextBoolean();

    when(todoListRepo.findById(1L)).thenReturn(Optional.of(list));
    when(todoListRepo.save(list)).thenReturn(savedList);
    when(todoListRepo.isListCompleted(1L)).thenReturn(completed);

    TodoListUpdateDto updateDto = mock(TodoListUpdateDto.class);
    when(updateDto.title()).thenReturn("nm");
    TodoListDto dto = new TodoListDto(1L, "nm", created, updated, completed);

    assertEquals(dto, service.updateTodoList(1L, updateDto).get());

    LocalDateTime after = LocalDateTime.now().plusNanos(1L);

    verify(todoListRepo).findById(1L);
    verify(list).setUpdated(argThat(arg -> arg.isAfter(before) && arg.isBefore(after)));
    verify(list).setTitle("nm");
    verifyNoMoreInteractions(list);
    verify(todoListRepo).save(list);
    verify(todoListRepo).isListCompleted(1L);
  }

  @Test
  void deleteTodoList() throws Exception {
    service.deleteTodoList(1L).get();

    verify(todoListRepo).deleteById(1L);
  }
}
