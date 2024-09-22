package com.tbaruth.todocore.repo;

import com.tbaruth.todocore.entity.TodoList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("unit-test")
public class TodoListRepoUTest {

  @Autowired
  private TodoListRepo repo;

  @Test
  void findByUserId() {
    List<TodoList> lists = repo.findByUserId(1L);
    assertEquals(1, lists.size());
    assertEquals(1L, lists.get(0).getId());
  }

  @Test
  void isListCompleted() {
    assertFalse(repo.isListCompleted(1L));
  }
}
