package com.tbaruth.todocore.controller;

import com.tbaruth.todocore.dto.TodoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/todo-lists")
@RestController
public class TodoItemController {

  @GetMapping("/{listId}/todos")
  public ResponseEntity<TodoDto> getTodos(@PathVariable String listId) {
    return null;
  }

  @GetMapping("/{listId}/todos/{itemId}")
  public ResponseEntity<?> getTodoItem(@PathVariable Long listId, @PathVariable String itemId) {
    return null;
  }

  @PostMapping("/{listId}/todos")
  public ResponseEntity<?> createTodoItem(@PathVariable Long listId, @RequestBody TodoDto todo) {
    return null;
  }

  @PutMapping("/{listId}/todos/{itemId}")
  public ResponseEntity<?> updateTodoItem(@PathVariable Long listId, @PathVariable Long itemId, @RequestBody TodoDto todo) {
    return null;
  }

  @DeleteMapping("/{listId}/todos/{itemId}")
  public ResponseEntity<?> deleteTodoItem(@PathVariable Long listId, @PathVariable Long itemId) {
    return null;
  }
}
