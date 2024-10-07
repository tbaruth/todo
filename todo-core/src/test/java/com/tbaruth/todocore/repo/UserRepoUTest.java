package com.tbaruth.todocore.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("unit-test")
public class UserRepoUTest {

  @Autowired
  private UserRepo repo;

  @Test
  void findByUsername() {
    assertEquals(1L, repo.findByUsername("bob").getId());
    assertNull(repo.findByUsername("bob@test.com"));
  }
}
