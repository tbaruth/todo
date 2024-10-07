package com.tbaruth.todocore.entity;

import com.tbaruth.todocore.entity.converter.TodoStatusConverter;
import com.tbaruth.todocore.enums.TodoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Table(name = "todo_item")
@Entity
public class TodoItem {

  private Long id;
  private String title;
  private String description;
  private String created;
  private String updated;
  private TodoStatus status;
  private TodoList list;

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

  public void setTitle(String title) {
    this.title = title;
  }

  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Column(name = "created")
  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  @Column(name = "updated")
  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  @Column(name = "status")
  @Convert(converter = TodoStatusConverter.class)
  public TodoStatus getStatus() {
    return status;
  }

  public void setStatus(TodoStatus status) {
    this.status = status;
  }

  @ManyToOne
  @JoinColumn(name = "list_id")
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
