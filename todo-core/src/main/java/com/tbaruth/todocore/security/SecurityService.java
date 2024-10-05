package com.tbaruth.todocore.security;

import com.tbaruth.todocore.entity.TodoItem;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.repo.TodoItemRepo;
import com.tbaruth.todocore.repo.TodoListRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SecurityService {

  private final TodoListRepo todoListRepo;
  private final TodoItemRepo todoItemRepo;

  public SecurityService(TodoListRepo todoListRepo, TodoItemRepo todoItemRepo) {
    this.todoListRepo = todoListRepo;
    this.todoItemRepo = todoItemRepo;
  }

  public boolean isAbleToViewTodoLists(Authentication auth) {
    return true;
  }

  public boolean isAbleToCreateTodoList(Authentication auth) {
    return true;
  }

  public boolean isAbleToEditTodoList(Long todoListId, Authentication auth) {
    TodoList list = todoListRepo.findById(todoListId).orElse(null);
    return list != null && StringUtils.equals(list.getUser().getEmail(), auth.getName());
  }

  public boolean isAbleToViewTodos(Long todoListId, Authentication auth) {
    TodoList list = todoListRepo.findById(todoListId).orElse(null);
    return list != null && StringUtils.equals(list.getUser().getEmail(), auth.getName());
  }

  public boolean isAbleToCreateTodoItem(Long todoListId, Authentication auth) {
    TodoList list = todoListRepo.findById(todoListId).orElse(null);
    return list != null && StringUtils.equals(list.getUser().getEmail(), auth.getName());
  }

  public boolean isAbleToViewTodoItem(Long todoListId, Long todoId, Authentication auth) {
    TodoItem todoItem = todoItemRepo.findById(todoId).orElse(null);
    return todoItem != null && Objects.equals(todoListId, todoItem.getList().getId()) && StringUtils.equals(todoItem.getList().getUser().getEmail(), auth.getName());
  }

  public boolean isAbleToEditTodoItem(Long todoListId, Long todoId, Authentication auth) {
    TodoItem todoItem = todoItemRepo.findById(todoId).orElse(null);
    return todoItem != null && Objects.equals(todoListId, todoItem.getList().getId()) && StringUtils.equals(todoItem.getList().getUser().getEmail(), auth.getName());
  }
}
