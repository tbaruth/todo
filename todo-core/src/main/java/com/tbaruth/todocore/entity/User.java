package com.tbaruth.todocore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity(name = "user")
public class User {

  private Long id;
  private List<TodoList> todoLists;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @OneToMany(mappedBy = "user")
  public List<TodoList> getTodoLists() {
    return todoLists;
  }

  public void setTodoLists(List<TodoList> todoLists) {
    this.todoLists = todoLists;
  }
}
