package com.tbaruth.todocore.entity;

import com.tbaruth.todocore.enums.TodoStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity(name = "todo_item")
public class TodoItem {

  private Long id;
  private String title;
  private String description;
  private String created;
  private String updated;
  private TodoStatus status;
  private TodoList list;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public TodoStatus getStatus() {
    return status;
  }

  public void setStatus(TodoStatus status) {
    this.status = status;
  }

  @ManyToOne
  @JoinColumn(name = "todo_list_id")
  public TodoList getList() {
    return list;
  }

  public void setList(TodoList list) {
    this.list = list;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TodoItem todoItem = (TodoItem) o;
    return Objects.equals(id, todoItem.id) && Objects.equals(title, todoItem.title) && Objects.equals(description, todoItem.description) && Objects.equals(created, todoItem.created) && Objects.equals(updated, todoItem.updated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, created, updated);
  }

  @Override
  public String toString() {
    return "TodoItem{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", created='" + created + '\'' +
        ", updated='" + updated + '\'' +
        '}';
  }
}
