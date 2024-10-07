package com.tbaruth.todocore.security;

import com.tbaruth.todocore.entity.TodoItem;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.repo.TodoItemRepo;
import com.tbaruth.todocore.repo.TodoListRepo;
import com.tbaruth.todocore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SecurityService {

  private final TodoListRepo todoListRepo;
  private final TodoItemRepo todoItemRepo;
  private final UserService userService;

  public SecurityService(TodoListRepo todoListRepo, TodoItemRepo todoItemRepo, UserService userService) {
    this.todoListRepo = todoListRepo;
    this.todoItemRepo = todoItemRepo;
    this.userService = userService;
  }

  public boolean isAbleToViewTodoLists(Authentication auth) {
    return true;
  }

  public boolean isAbleToCreateTodoList(Authentication auth) {
    return true;
  }

  public boolean isAbleToEditTodoList(Long todoListId, Authentication auth) {
    TodoList list = todoListRepo.findById(todoListId).orElse(null);
    return list != null && Objects.equals(list.getUser().getId(), userService.getUserId(auth));
  }

  public boolean isAbleToViewTodos(Long todoListId, Authentication auth) {
    TodoList list = todoListRepo.findById(todoListId).orElse(null);
    return list != null && Objects.equals(list.getUser().getId(), userService.getUserId(auth));
  }

  public boolean isAbleToCreateTodoItem(Long todoListId, Authentication auth) {
    TodoList list = todoListRepo.findById(todoListId).orElse(null);
    return list != null && Objects.equals(list.getUser().getId(), userService.getUserId(auth));
  }

  public boolean isAbleToViewTodoItem(Long todoListId, Long todoId, Authentication auth) {
    TodoItem todoItem = todoItemRepo.findById(todoId).orElse(null);
    return todoItem != null && Objects.equals(todoListId, todoItem.getList().getId()) && Objects.equals(todoItem.getList().getUser().getId(), userService.getUserId(auth));
  }

  public boolean isAbleToEditTodoItem(Long todoListId, Long todoId, Authentication auth) {
    TodoItem todoItem = todoItemRepo.findById(todoId).orElse(null);
    return todoItem != null && Objects.equals(todoListId, todoItem.getList().getId()) && Objects.equals(todoItem.getList().getUser().getId(), userService.getUserId(auth));
  }

  public boolean isAbleToToggleDarkMode(Authentication auth) {
    return userService.getUserId(auth) != null;
  }
}
