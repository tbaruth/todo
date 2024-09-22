package com.tbaruth.todocore.entity.converter;

import com.tbaruth.todocore.enums.TodoStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter
public class TodoStatusConverter implements AttributeConverter<TodoStatus, Integer> {

  @Override
  public Integer convertToDatabaseColumn(TodoStatus status) {
    if (status == null) {
      return null;
    }
    return status.getId();
  }

  @Override
  public TodoStatus convertToEntityAttribute(Integer id) {
    if (id == null) {
      return null;
    }

    return Stream.of(TodoStatus.values())
        .filter(value -> value.getId() == id)
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
