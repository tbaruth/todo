package com.tbaruth.todocore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "todo_list")
@Entity
public class TodoList {

  private Long id;
  private String title;
  private LocalDateTime created;
  private LocalDateTime updated;
  private User user;
  private List<TodoItem> todoItems;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String name) {
    this.title = name;
  }

  @Column(name = "created")
  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  @Column(name = "updated")
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
    return Objects.equals(id, todoList.id) && Objects.equals(title, todoList.title) && Objects.equals(created, todoList.created) && Objects.equals(updated, todoList.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, created, updated);
  }

  @Override
  public String toString() {
    return "TodoList{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", created='" + created + '\'' +
        ", updated='" + updated + '\'' +
        '}';
  }
}
