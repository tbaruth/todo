package com.tbaruth.todocore.validator;

import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.service.TodoListService;
import com.tbaruth.todocore.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class TodoListValidator {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListValidator.class);

  private final TodoListService todoListService;
  private final UserService userService;
  private final ExecutorService genExecutor;

  public TodoListValidator(TodoListService todoListService, UserService userService, ExecutorService genExecutor) {
    this.todoListService = todoListService;
    this.userService = userService;
    this.genExecutor = genExecutor;
  }

  public boolean validateCreate(TodoListCreateDto createDto) {
    Long userId = userService.getCurrentUserId();
    if (StringUtils.isBlank(createDto.title())) {
      LOG.warn("{} could not create todo list because title was blank", userId);
      return false;
    } else if (StringUtils.length(createDto.title()) > 250) {
      LOG.warn("{} could not create todo list because title was too long", userId);
      return false;
    }
    return true;
  }

  public Future<Boolean> validateUpdate(Long todoListId, TodoListUpdateDto updateDto) {
    return genExecutor.submit(() -> {
      Long userId = userService.getCurrentUserId();
      if (StringUtils.isBlank(updateDto.title())) {
        LOG.warn("{} could not update todo list {} because title was blank", userId, todoListId);
        return false;
      } else if (StringUtils.length(updateDto.title()) > 250) {
        LOG.warn("{} could not update todo list {} because title was too long", userId, todoListId);
        return false;
      } else {
        TodoList todoList = todoListService.getTodoList(todoListId).get();
        if (todoList == null) {
          LOG.warn("{} could not update todo list {} because the list did not exist", userId, todoListId);
          return false;
        } else if (!Objects.equals(todoList.getUser().getId(), userId)) {
          LOG.warn("{} could not update todo list {} because the list belongs to somebody else", userId, todoListId);
          return false;
        }
      }
      return true;
    });
  }

  public Future<Boolean> validateDelete(Long todoListId) {
    return genExecutor.submit(() -> {
      Long userId = userService.getCurrentUserId();
      TodoList todoList = todoListService.getTodoList(todoListId).get();
      if (todoList == null) {
        LOG.warn("{} could not delete todo list {} because the list did not exist", userId, todoListId);
        return false;
      } else if (!Objects.equals(todoList.getUser().getId(), userId)) {
        LOG.warn("{} could not delete todo list {} because the list belongs to somebody else", userId, todoListId);
        return false;
      }
      return true;
    });
  }
}
