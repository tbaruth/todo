package com.tbaruth.todocore.dto;

public record UserDto(Long id, String username, String firstName, String lastName, String email, boolean darkMode) {
}
