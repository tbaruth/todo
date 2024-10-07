package com.tbaruth.todocore.controller;

import com.tbaruth.todocore.dto.UserDto;
import com.tbaruth.todocore.dto.incoming.DarkModeDto;
import com.tbaruth.todocore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@RequestMapping("/users")
@RestController
public class UserController {

  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;
  private final ExecutorService genExecutorService;

  public UserController(UserService userService, ExecutorService genExecutorService) {
    this.userService = userService;
    this.genExecutorService = genExecutorService;
  }

  @GetMapping
  public DeferredResult<ResponseEntity<UserDto>> getSelf() {
    DeferredResult<ResponseEntity<UserDto>> result = new DeferredResult<>();
    genExecutorService.submit(() -> {
      try {
        UserDto userDto = userService.getOrCreateUser(SecurityContextHolder.getContext().getAuthentication()).get();
        result.setResult(new ResponseEntity<>(userDto, HttpStatus.OK));
      } catch (InterruptedException | ExecutionException ex) {
        LOG.error("User {} could not get self", SecurityContextHolder.getContext().getAuthentication().getName(), ex);
        result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
      }
    });
    return result;
  }

  @PutMapping
  @PreAuthorize("@securityService.isAbleToToggleDarkMode(authentication)")
  public DeferredResult<ResponseEntity<UserDto>> toggleDarkMode(@RequestBody DarkModeDto darkModeDto) {
    DeferredResult<ResponseEntity<UserDto>> result = new DeferredResult<>();
    genExecutorService.submit(() -> {
      try {
        UserDto userDto = userService.toggleDarkMode(userService.getCurrentUserId(), darkModeDto.enabled()).get();
        result.setResult(new ResponseEntity<>(userDto, HttpStatus.ACCEPTED));
      } catch (InterruptedException | ExecutionException ex) {
        LOG.error("User {} could not toggle dark mode", userService.getCurrentUserId(), ex);
        result.setResult(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
      }
    });
    return result;
  }
}
