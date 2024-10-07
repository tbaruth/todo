package com.tbaruth.todocore.service;

import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.repo.TodoItemRepo;
import com.tbaruth.todocore.repo.TodoListRepo;
import com.tbaruth.todocore.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class TodoListService {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListService.class);
  private final TodoListRepo todoListRepo;
  private final TodoItemRepo todoItemRepo;
  private final UserRepo userRepo;
  private final ExecutorService genExecutor;

  public TodoListService(TodoListRepo todoListRepo, TodoItemRepo todoItemRepo, UserRepo userRepo, ExecutorService genExecutor) {
    this.todoListRepo = todoListRepo;
    this.todoItemRepo = todoItemRepo;
    this.userRepo = userRepo;
    this.genExecutor = genExecutor;
  }

  public List<Future<TodoListDto>> getTodoLists(Long userId) {
    List<Future<TodoListDto>> resultsFutures = new ArrayList<>();
    try {
      List<TodoList> todoLists = todoListRepo.findByUserId(userId);
      for (TodoList todoList : todoLists) {
        resultsFutures.add(genExecutor.submit(() -> new TodoListDto(todoList.getId(), todoList.getTitle(), todoList.getCreated(), todoList.getUpdated(),
            todoListRepo.getItemsCount(todoList.getId()), todoListRepo.getCompletedCount(todoList.getId()))));
      }
    } catch (Exception ex) {
      LOG.error("Exception fetching todos list!");
      throw new RuntimeException(ex);
    }
    return resultsFutures;
  }

  public Future<TodoList> getTodoList(Long todoListId) {
    return genExecutor.submit(() -> todoListRepo.findById(todoListId).orElse(null));
  }

  public Future<TodoListDto> createTodoList(Long userId, TodoListCreateDto dto) {
    return genExecutor.submit(() -> {
      LocalDateTime now = LocalDateTime.now();
      TodoList list = new TodoList();
      list.setCreated(now);
      list.setUpdated(now);
      list.setTitle(dto.title());
      list.setUser(userRepo.findById(userId).orElseThrow(() -> new IllegalStateException("User with ID " + userId + " should have been present, but was not!")));
      list = todoListRepo.save(list);
      return new TodoListDto(list.getId(), list.getTitle(), list.getCreated(), list.getUpdated(), 0, 0);
    });
  }

  public Future<TodoListDto> updateTodoList(Long listId, TodoListUpdateDto dto) {
    return genExecutor.submit(() -> {
      LocalDateTime now = LocalDateTime.now();
      TodoList list = todoListRepo.findById(listId).orElseThrow(() -> new IllegalStateException("List with ID " + listId + " should have been present, but was not!"));
      list.setUpdated(now);
      list.setTitle(dto.title());
      list = todoListRepo.save(list);
      return new TodoListDto(list.getId(), list.getTitle(), list.getCreated(), list.getUpdated(), todoListRepo.getItemsCount(list.getId()), todoListRepo.getCompletedCount(list.getId()));
    });
  }

  public Future<Void> deleteTodoList(Long listId) {
    return genExecutor.submit(() -> {
      todoListRepo.deleteById(listId);
      return null;
    });
  }
}
