package com.tbaruth.todocore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

@Table(name = "todo_user")
@Entity
public class User {

  private Long id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private boolean darkMode;
  private List<TodoList> todoLists;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "first_name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Column(name = "last_name")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Column(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "dark_mode")
  public boolean isDarkMode() {
    return darkMode;
  }

  public void setDarkMode(boolean darkMode) {
    this.darkMode = darkMode;
  }

  @OneToMany(mappedBy = "user")
  public List<TodoList> getTodoLists() {
    return todoLists;
  }

  public void setTodoLists(List<TodoList> todoLists) {
    this.todoLists = todoLists;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return darkMode == user.darkMode && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, firstName, lastName, email, darkMode);
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", darkMode=" + darkMode +
        '}';
  }
}
