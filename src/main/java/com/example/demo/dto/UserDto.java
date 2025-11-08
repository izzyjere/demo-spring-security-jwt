package com.example.demo.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.example.demo.models.User}
 */
public record UserDto(Long id, String firstName, String lastName, String email) implements Serializable {
}