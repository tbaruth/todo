package com.tbaruth.todocore.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TodoStatus {

  TODO(1, "To Do"),
  IN_PROGRESS(2, "In Progress"),
  DONE(3, "Done");

  private int id;
  private String name;

  TodoStatus(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  @JsonValue
  public String getName() {
    return name;
  }
}
