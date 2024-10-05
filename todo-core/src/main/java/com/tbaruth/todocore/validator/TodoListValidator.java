package com.tbaruth.todocore.validator;

import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.service.TodoListService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class TodoListValidator {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListValidator.class);

  private final TodoListService todoListService;

  public TodoListValidator(TodoListService todoListService) {
    this.todoListService = todoListService;
  }

  public boolean validateCreate(TodoListCreateDto createDto) {
    if (StringUtils.isBlank(createDto.title())) {
      LOG.warn("{} could not create todo list because title was blank", SecurityContextHolder.getContext().getAuthentication().getName());
      return false;
    } else if (StringUtils.length(createDto.title()) > 250) {
      LOG.warn("{} could not create todo list because title was too long", SecurityContextHolder.getContext().getAuthentication().getName());
      return false;
    }
    return true;
  }

  public boolean validateUpdate(Long todoListId, TodoListUpdateDto updateDto) throws ExecutionException, InterruptedException {
    if (StringUtils.isBlank(updateDto.title())) {
      LOG.warn("{} could not update todo list {} because title was blank", SecurityContextHolder.getContext().getAuthentication().getName(), todoListId);
      return false;
    } else if (StringUtils.length(updateDto.title()) > 250) {
      LOG.warn("{} could not update todo list {} because title was too long", SecurityContextHolder.getContext().getAuthentication().getName(), todoListId);
      return false;
    } else {
      TodoList todoList = todoListService.getTodoList(todoListId).get();
      if (todoList == null) {
        LOG.warn("{} could not update todo list {} because the list did not exist", SecurityContextHolder.getContext().getAuthentication().getName(), todoListId);
        return false;
      } else if (!StringUtils.equals(todoList.getUser().getEmail(), SecurityContextHolder.getContext().getAuthentication().getName())) {
        LOG.warn("{} could not update todo list {} because the list belongs to somebody else", SecurityContextHolder.getContext().getAuthentication().getName(), todoListId);
        return false;
      }
    }
    return true;
  }

  public boolean validateDelete(Long todoListId) throws ExecutionException, InterruptedException {
    TodoList todoList = todoListService.getTodoList(todoListId).get();
    if (todoList == null) {
      LOG.warn("{} could not delete todo list {} because the list did not exist", SecurityContextHolder.getContext().getAuthentication().getName(), todoListId);
      return false;
    } else if (!StringUtils.equals(todoList.getUser().getEmail(), SecurityContextHolder.getContext().getAuthentication().getName())) {
      LOG.warn("{} could not delete todo list {} because the list belongs to somebody else", SecurityContextHolder.getContext().getAuthentication().getName(), todoListId);
      return false;
    }
    return true;
  }
}
