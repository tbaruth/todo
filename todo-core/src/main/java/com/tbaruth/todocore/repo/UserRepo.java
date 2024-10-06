package com.tbaruth.todocore.repo;

import com.tbaruth.todocore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User, Long> {

  @Query("select u from User u where u.username = :username")
  User findByUsername(@Param("username") String username);
}
