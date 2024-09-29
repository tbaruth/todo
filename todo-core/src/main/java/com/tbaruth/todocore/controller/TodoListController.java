package com.tbaruth.todocore.controller;

import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.security.SecurityService;
import com.tbaruth.todocore.service.TodoListService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@RequestMapping("/api/todo-lists")
@RestController
public class TodoListController {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListController.class);
  private final TodoListService todoListService;
  private final ExecutorService genExecutorService;

  public TodoListController(TodoListService todoListService, ExecutorService genExecutorService) {
    this.todoListService = todoListService;
    this.genExecutorService = genExecutorService;
  }

  @GetMapping
  @PreAuthorize("@securityService.isAbleToViewTodoLists(authentication)")
  public DeferredResult<ResponseEntity<List<TodoListDto>>> getTodoLists() {
    DeferredResult<ResponseEntity<List<TodoListDto>>> result = new DeferredResult<>();
    genExecutorService.submit(() -> {
      List<TodoListDto> dtos = new ArrayList<>();
      try {
        for (var future : todoListService.getTodoLists(1L)) {
          dtos.add(future.get());
        }
        result.setResult(new ResponseEntity<>(dtos, HttpStatus.OK));
      } catch (InterruptedException | ExecutionException ex) {
        LOG.error("User " + 1L + " could not get TODO lists", ex);
        result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
      }
    });
    return result;
  }

  @PostMapping
  @PreAuthorize("@securityService.isAbleToCreateTodoList(authentication)")
  public ResponseEntity<TodoListDto> createTodoList(@RequestBody TodoListDto todoListDto) {
    return new ResponseEntity<>(new TodoListDto(1L, null, null, null, false), HttpStatus.ACCEPTED);
  }

  @PutMapping("/{listId}")
  @PreAuthorize("@securityService.isAbleToEditTodoList(#listId, authentication)")
  public ResponseEntity<TodoListDto> updateTodoList(@PathVariable String listId, @RequestBody TodoListDto todoListDto) {
    return null;
  }

  @DeleteMapping("/{listId}")
  @PreAuthorize("@securityService.isAbleToEditTodoList(#listId, authentication)")
  public ResponseEntity<?> deleteTodoList(@PathVariable String listId) {
    return null;
  }
}
