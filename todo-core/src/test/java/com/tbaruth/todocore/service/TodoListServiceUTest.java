package com.tbaruth.todocore.service;

import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.repo.TodoItemRepo;
import com.tbaruth.todocore.repo.TodoListRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

  @Test
  void getTodoLists() throws Exception {
    LocalDateTime created = LocalDateTime.now().minusDays(2L);
    LocalDateTime updated = LocalDateTime.now().minusDays(1L);
    TodoList todoList = mock(TodoList.class);
    when(todoList.getId()).thenReturn(2L);
    when(todoList.getName()).thenReturn("nm");
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
}
