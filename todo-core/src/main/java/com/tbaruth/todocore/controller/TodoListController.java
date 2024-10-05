package com.tbaruth.todocore.controller;

import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.service.TodoListService;
import com.tbaruth.todocore.validator.TodoListValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

@RequestMapping("/todo-lists")
@RestController
public class TodoListController {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListController.class);
  private final TodoListService todoListService;
  private final ExecutorService genExecutorService;
  private final TodoListValidator validator;

  public TodoListController(TodoListService todoListService, ExecutorService genExecutorService, TodoListValidator validator) {
    this.todoListService = todoListService;
    this.genExecutorService = genExecutorService;
    this.validator = validator;
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
        LOG.error("User {} could not get TODO lists", SecurityContextHolder.getContext().getAuthentication().getName(), ex);
        result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
      }
    });
    return result;
  }

  @PostMapping
  @PreAuthorize("@securityService.isAbleToCreateTodoList(authentication)")
  public DeferredResult<ResponseEntity<TodoListDto>> createTodoList(@RequestBody TodoListCreateDto createDto) {
    DeferredResult<ResponseEntity<TodoListDto>> result = new DeferredResult<>();
    genExecutorService.submit(() -> {
      boolean valid = validator.validateCreate(createDto);
      if (valid) {
        try {
          TodoListDto dto = todoListService.createTodoList(createDto).get();
          result.setResult(new ResponseEntity<>(dto, HttpStatus.CREATED));
        } catch (InterruptedException | ExecutionException ex) {
          LOG.error("User {} could not create TODO list", SecurityContextHolder.getContext().getAuthentication().getName(), ex);
          result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }
      } else {
        result.setResult(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
      }
    });
    return result;
  }

  @PutMapping("/{listId}")
  @PreAuthorize("@securityService.isAbleToEditTodoList(#listId, authentication)")
  public DeferredResult<ResponseEntity<TodoListDto>> updateTodoList(@PathVariable Long listId, @RequestBody TodoListUpdateDto todoListUpdateDto) {
    DeferredResult<ResponseEntity<TodoListDto>> result = new DeferredResult<>();
    genExecutorService.submit(() -> {
      try {
        boolean valid = validator.validateUpdate(listId, todoListUpdateDto).get();
        if (valid) {
          TodoListDto dto = todoListService.updateTodoList(listId, todoListUpdateDto).get();
          result.setResult(new ResponseEntity<>(dto, HttpStatus.ACCEPTED));
        } else {
          result.setResult(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
      } catch (InterruptedException | ExecutionException ex) {
        LOG.error("User {} could not update TODO list {}", SecurityContextHolder.getContext().getAuthentication().getName(), listId, ex);
        result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
      }
    });
    return result;
  }

  @DeleteMapping("/{listId}")
  @PreAuthorize("@securityService.isAbleToEditTodoList(#listId, authentication)")
  public DeferredResult<ResponseEntity<?>> deleteTodoList(@PathVariable Long listId) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    genExecutorService.submit(() -> {
      try {
        boolean valid = validator.validateDelete(listId).get();
        if (valid) {
          todoListService.deleteTodoList(listId).get();
          result.setResult(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        } else {
          result.setResult(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
      } catch (InterruptedException | ExecutionException ex) {
        LOG.error("User {} could not delete TODO list {}", SecurityContextHolder.getContext().getAuthentication().getName(), listId, ex);
        result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
      }
    });
    return result;
  }
}
