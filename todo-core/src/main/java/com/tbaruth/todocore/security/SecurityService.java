package com.tbaruth.todocore.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

  public boolean isAbleToViewTodoLists(Authentication auth) {
    return true;
  }

  public boolean isAbleToCreateTodoList(Authentication auth) {
    return true;
  }

  public boolean isAbleToEditTodoList(Long todoListId, Authentication auth) {
    return true;
  }

  public boolean isAbleToViewTodos(Long todoListId, Authentication auth) {
    return true;
  }

  public boolean isAbleToCreateTodoItem(Long todoListId, Authentication auth) {
    return true;
  }

  public boolean isAbleToViewTodoItem(Long todoListId, Long todoId, Authentication auth) {
    return true;
  }

  public boolean isAbleToEditTodoItem(Long todoListId, Long todoId, Authentication auth) {
    return true;
  }
}
