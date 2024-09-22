package com.tbaruth.todocore.repo;

import com.tbaruth.todocore.entity.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoListRepo extends JpaRepository<TodoList, Long> {

  @Query("select tl from TodoList tl where tl.user.id = :userId")
  List<TodoList> findByUserId(@Param("userId") Long userId);

  @Query("select count(ti.id) > 0 from TodoItem ti where ti.todoList.id = :id and ti.status <> com.tbaruth.todocore.enums.TodoStatus.DONE")
  boolean isListCompleted(@Param("id") Long id);
}
