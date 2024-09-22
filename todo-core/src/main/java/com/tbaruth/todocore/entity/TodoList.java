package com.tbaruth.todocore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity(name = "todo_list")
public class TodoList {

  private Long id;
  private String name;
  private LocalDateTime created;
  private LocalDateTime updated;
  private User user;
  private List<TodoItem> todoItems;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public LocalDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(LocalDateTime updated) {
    this.updated = updated;
  }

  @ManyToOne
  @JoinColumn(name = "user_id")
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @OneToMany(mappedBy = "list")
  public List<TodoItem> getTodoItems() {
    return todoItems;
  }

  public void setTodoItems(List<TodoItem> todoItems) {
    this.todoItems = todoItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TodoList todoList = (TodoList) o;
    return Objects.equals(id, todoList.id) && Objects.equals(name, todoList.name) && Objects.equals(created, todoList.created) && Objects.equals(updated, todoList.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, created, updated);
  }

  @Override
  public String toString() {
    return "TodoList{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", created='" + created + '\'' +
        ", updated='" + updated + '\'' +
        '}';
  }
}
