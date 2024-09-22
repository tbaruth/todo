package com.tbaruth.todocore.repo;

import com.tbaruth.todocore.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoItemRepo extends JpaRepository<TodoItem, Long> {

}
