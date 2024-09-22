package com.tbaruth.todocore.repo;

import com.tbaruth.todocore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
