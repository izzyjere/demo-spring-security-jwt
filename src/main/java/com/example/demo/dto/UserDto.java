package com.example.demo.dto;

import com.example.demo.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link User}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}