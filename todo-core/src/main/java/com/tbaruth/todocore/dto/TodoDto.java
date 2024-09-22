package com.tbaruth.todocore.dto;

import java.time.LocalDateTime;

public record TodoDto(Long id, String title, String description, LocalDateTime created, LocalDateTime updated, String status) {}
