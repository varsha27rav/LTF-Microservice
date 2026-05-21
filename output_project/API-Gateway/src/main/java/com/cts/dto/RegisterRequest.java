package com.cts.dto;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Name should not be empty")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be empty")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;


@Pattern(
    regexp = "^(\\+91[- ]?)?[6-9][0-9]{9}$",
    message = "Phone must be in format +91-XXXXXXXXXX"
)
    private String phoneNumber;
}