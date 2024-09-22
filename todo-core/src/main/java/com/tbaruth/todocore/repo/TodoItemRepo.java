package com.tbaruth.todocore.repo;

import com.tbaruth.todocore.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoItemRepo extends JpaRepository<TodoItem, Long> {

}
