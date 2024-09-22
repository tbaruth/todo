package com.tbaruth.todocore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbaruth.todocore.config.ExecutorConfig;
import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.entity.TodoList;
import com.tbaruth.todocore.service.TodoListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@WebMvcTest({TodoListController.class, ExecutorConfig.class})
@WithMockUser
public class TodoListControllerUTest {

  @MockBean
  private TodoListService todoListService;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private ExecutorService executorService;
  @Autowired
  private MockMvc mockMvc;
  private Authentication auth;

  @BeforeEach
  void setUp() {
    auth = SecurityContextHolder.getContext().getAuthentication();
  }

  @Nested
  @DisplayName("getTodoLists")
  class GetTodoListsTests {

    @Test
    void successShouldReturnDtosWithOK() throws Exception {
      TodoListDto dto = new TodoListDto(5L, "nm", null, null, true);

      when(todoListService.getTodoLists(1L)).thenReturn(List.of(CompletableFuture.supplyAsync(() -> dto)));

      MvcResult result = mockMvc.perform(get("/api/todo-lists")
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(List.of(dto))));

      verify(todoListService).getTodoLists(1L);
    }

    @Test
    void failureShouldReturnInternalServerError() {

    }
  }

  @Nested
  @DisplayName("createTodoList")
  class CreateTodoListTests {

    @Test
    void successShouldReturnDtoWithAccepted() throws Exception {
      TodoListDto dto = new TodoListDto(1L, null, null, null, false);

      mockMvc.perform(post("/api/todo-lists")
              .with(csrf())
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andDo(print())
          .andExpect(status().isAccepted())
          .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void failureShouldReturnInternalServerError() {

    }
  }

}
