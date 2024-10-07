package com.tbaruth.todocore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbaruth.todocore.config.ExecutorConfig;
import com.tbaruth.todocore.config.SecurityConfig;
import com.tbaruth.todocore.dto.TodoListDto;
import com.tbaruth.todocore.dto.incoming.TodoListCreateDto;
import com.tbaruth.todocore.dto.incoming.TodoListUpdateDto;
import com.tbaruth.todocore.security.SecurityService;
import com.tbaruth.todocore.service.TodoListService;
import com.tbaruth.todocore.service.UserService;
import com.tbaruth.todocore.validator.TodoListValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({TodoListController.class, ExecutorConfig.class, SecurityConfig.class})
@WithMockUser
@EnableMethodSecurity
public class TodoListControllerUTest {

  @MockBean
  private TodoListService todoListService;
  @MockBean
  private UserService userService;
  @MockBean
  private TodoListValidator todoListValidator;
  @MockBean(name = "securityService")
  private SecurityService securityService;
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

  @AfterEach
  void tearDown() {
    verifyNoMoreInteractions(todoListService, userService, todoListValidator, securityService);
  }

  @Nested
  @DisplayName("getTodoLists")
  class GetTodoListsTests {

    @Test
    void successShouldReturnDtosWithOK() throws Exception {
      TodoListDto dto = new TodoListDto(5L, "nm", null, null, 1, 1);

      when(securityService.isAbleToViewTodoLists(auth)).thenReturn(true);
      when(userService.getCurrentUserId()).thenReturn(1L);
      when(todoListService.getTodoLists(1L)).thenReturn(List.of(CompletableFuture.supplyAsync(() -> dto)));

      MvcResult result = mockMvc.perform(get("/todo-lists")
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(List.of(dto))));

      verify(securityService).isAbleToViewTodoLists(auth);
      verify(userService).getCurrentUserId();
      verify(todoListService).getTodoLists(1L);
    }

    @Test
    void failureShouldReturnInternalServerError() throws Exception {
      @SuppressWarnings("unchecked")
      CompletableFuture<TodoListDto> future = mock(CompletableFuture.class);
      when(future.get()).thenThrow(new InterruptedException("expected"));

      when(securityService.isAbleToViewTodoLists(auth)).thenReturn(true);
      when(userService.getCurrentUserId()).thenReturn(1L);
      when(todoListService.getTodoLists(1L)).thenReturn(List.of(future));

      MvcResult result = mockMvc.perform(get("/todo-lists")
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isInternalServerError());

      verify(securityService).isAbleToViewTodoLists(auth);
      verify(todoListService).getTodoLists(1L);
      verify(userService, times(2)).getCurrentUserId();
    }

    @Test
    void notAuthorizedShouldFail() throws Exception {
      mockMvc.perform(get("/todo-lists")
              .principal(auth))
          .andExpect(status().isForbidden());

      verify(securityService).isAbleToViewTodoLists(auth);
    }
  }

  @Nested
  @DisplayName("createTodoList")
  class CreateTodoListTests {

    @Test
    void successShouldReturnDtoWithCreated() throws Exception {
      TodoListCreateDto dto = new TodoListCreateDto("asdf");
      TodoListDto returnDto = new TodoListDto(5L, "nm", null, null, 2, 1);

      when(securityService.isAbleToCreateTodoList(auth)).thenReturn(true);
      when(todoListValidator.validateCreate(dto)).thenReturn(true);
      when(userService.getCurrentUserId()).thenReturn(1L);
      when(todoListService.createTodoList(1L, dto)).thenReturn(CompletableFuture.supplyAsync(() -> returnDto));

      MvcResult result = mockMvc.perform(post("/todo-lists")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isCreated())
          .andExpect(content().json(objectMapper.writeValueAsString(returnDto)));

      verify(securityService).isAbleToCreateTodoList(auth);
      verify(todoListValidator).validateCreate(dto);
      verify(userService).getCurrentUserId();
      verify(todoListService).createTodoList(1L, dto);
    }

    @Test
    void failureShouldReturnInternalServerError() throws Exception {
      TodoListCreateDto dto = new TodoListCreateDto("asdf");

      @SuppressWarnings("unchecked")
      CompletableFuture<TodoListDto> future = mock(CompletableFuture.class);
      when(future.get()).thenThrow(new InterruptedException("expected"));

      when(securityService.isAbleToCreateTodoList(auth)).thenReturn(true);
      when(todoListValidator.validateCreate(dto)).thenReturn(true);
      when(userService.getCurrentUserId()).thenReturn(1L);
      when(todoListService.createTodoList(1L, dto)).thenReturn(future);

      MvcResult result = mockMvc.perform(post("/todo-lists")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isInternalServerError());

      verify(securityService).isAbleToCreateTodoList(auth);
      verify(todoListValidator).validateCreate(dto);
      verify(todoListService).createTodoList(1L, dto);
      verify(userService, times(2)).getCurrentUserId();
    }

    @Test
    void validationFailureShouldReturnBadRequest() throws Exception {
      TodoListCreateDto dto = new TodoListCreateDto("asdf");

      when(securityService.isAbleToCreateTodoList(auth)).thenReturn(true);
      when(todoListValidator.validateCreate(dto)).thenReturn(false);

      MvcResult result = mockMvc.perform(post("/todo-lists")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isBadRequest());

      verify(securityService).isAbleToCreateTodoList(auth);
      verify(todoListValidator).validateCreate(dto);
    }

    @Test
    void notAuthorizedShouldFail() throws Exception {
      TodoListCreateDto dto = new TodoListCreateDto("asdf");

      mockMvc.perform(post("/todo-lists")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andExpect(status().isForbidden());

      verify(securityService).isAbleToCreateTodoList(auth);
    }
  }

  @Nested
  @DisplayName("updateTodoList")
  class UpdateTodoListTests {

    @Test
    void successShouldReturnDtoWithAccepted() throws Exception {
      TodoListUpdateDto dto = new TodoListUpdateDto("asdf");
      TodoListDto returnDto = new TodoListDto(5L, "nm", null, null, 3, 2);

      when(securityService.isAbleToEditTodoList(1L, auth)).thenReturn(true);
      when(todoListValidator.validateUpdate(1L, dto)).thenReturn(CompletableFuture.supplyAsync(() -> true));
      when(todoListService.updateTodoList(1L, dto)).thenReturn(CompletableFuture.supplyAsync(() -> returnDto));

      MvcResult result = mockMvc.perform(put("/todo-lists/{listId}", "1")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isAccepted())
          .andExpect(content().json(objectMapper.writeValueAsString(returnDto)));

      verify(securityService).isAbleToEditTodoList(1L, auth);
      verify(todoListValidator).validateUpdate(1L, dto);
      verify(todoListService).updateTodoList(1L, dto);
    }

    @Test
    void failureShouldReturnInternalServerError() throws Exception {
      TodoListUpdateDto dto = new TodoListUpdateDto("asdf");

      @SuppressWarnings("unchecked")
      CompletableFuture<TodoListDto> future = mock(CompletableFuture.class);
      when(future.get()).thenThrow(new InterruptedException("expected"));

      when(securityService.isAbleToEditTodoList(1L, auth)).thenReturn(true);
      when(todoListValidator.validateUpdate(1L, dto)).thenReturn(CompletableFuture.supplyAsync(() -> true));
      when(todoListService.updateTodoList(1L, dto)).thenReturn(future);

      MvcResult result = mockMvc.perform(put("/todo-lists/{listId}", "1")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isInternalServerError());

      verify(securityService).isAbleToEditTodoList(1L, auth);
      verify(todoListValidator).validateUpdate(1L, dto);
      verify(todoListService).updateTodoList(1L, dto);
      verify(userService).getCurrentUserId();
    }

    @Test
    void validationFailureShouldReturnBadRequest() throws Exception {
      TodoListUpdateDto dto = new TodoListUpdateDto("asdf");

      when(securityService.isAbleToEditTodoList(1L, auth)).thenReturn(true);
      when(todoListValidator.validateUpdate(1L, dto)).thenReturn(CompletableFuture.supplyAsync(() -> false));

      MvcResult result = mockMvc.perform(put("/todo-lists/{listId}", "1")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isBadRequest());

      verify(securityService).isAbleToEditTodoList(1L, auth);
      verify(todoListValidator).validateUpdate(1L, dto);
    }

    @Test
    void notAuthorizedShouldFail() throws Exception {
      TodoListUpdateDto dto = new TodoListUpdateDto("asdf");

      mockMvc.perform(put("/todo-lists/{listId}", "1")
              .content(objectMapper.writeValueAsString(dto))
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andExpect(status().isForbidden());

      verify(securityService).isAbleToEditTodoList(1L, auth);
    }
  }

  @Nested
  @DisplayName("deleteTodoList")
  class DeleteTodoListTests {

    @Test
    void successShouldReturnNoContent() throws Exception {
      when(securityService.isAbleToEditTodoList(1L, auth)).thenReturn(true);
      when(todoListValidator.validateDelete(1L)).thenReturn(CompletableFuture.supplyAsync(() -> true));
      when(todoListService.deleteTodoList(1L)).thenReturn(CompletableFuture.supplyAsync(() -> null));

      MvcResult result = mockMvc.perform(delete("/todo-lists/{listId}", "1")
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isNoContent());

      verify(securityService).isAbleToEditTodoList(1L, auth);
      verify(todoListValidator).validateDelete(1L);
      verify(todoListService).deleteTodoList(1L);
    }

    @Test
    void failureShouldReturnInternalServerError() throws Exception {
      @SuppressWarnings("unchecked")
      CompletableFuture<Void> future = mock(CompletableFuture.class);
      when(future.get()).thenThrow(new InterruptedException("expected"));

      when(securityService.isAbleToEditTodoList(1L, auth)).thenReturn(true);
      when(todoListValidator.validateDelete(1L)).thenReturn(CompletableFuture.supplyAsync(() -> true));
      when(todoListService.deleteTodoList(1L)).thenReturn(future);

      MvcResult result = mockMvc.perform(delete("/todo-lists/{listId}", "1")
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isInternalServerError());

      verify(securityService).isAbleToEditTodoList(1L, auth);
      verify(todoListValidator).validateDelete(1L);
      verify(todoListService).deleteTodoList(1L);
      verify(userService).getCurrentUserId();
    }

    @Test
    void validationFailureShouldReturnBadRequest() throws Exception {
      when(securityService.isAbleToEditTodoList(1L, auth)).thenReturn(true);
      when(todoListValidator.validateDelete(1L)).thenReturn(CompletableFuture.supplyAsync(() -> false));

      MvcResult result = mockMvc.perform(delete("/todo-lists/{listId}", "1")
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andReturn();

      mockMvc
          .perform(asyncDispatch(result))
          .andDo(print())
          .andExpect(status().isBadRequest());

      verify(securityService).isAbleToEditTodoList(1L, auth);
      verify(todoListValidator).validateDelete(1L);
    }

    @Test
    void notAuthorizedShouldFail() throws Exception {
      mockMvc.perform(delete("/todo-lists/{listId}", "1")
              .contentType(MediaType.APPLICATION_JSON)
              .principal(auth))
          .andExpect(status().isForbidden());

      verify(securityService).isAbleToEditTodoList(1L, auth);
    }
  }
}
