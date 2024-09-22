package com.tbaruth.todocore.dto;

import java.time.LocalDateTime;

public record TodoListDto(Long id, String title, LocalDateTime created, LocalDateTime updated, boolean completed) {
}
