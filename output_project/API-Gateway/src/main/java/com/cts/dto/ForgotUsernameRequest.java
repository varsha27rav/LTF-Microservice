package com.cts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotUsernameRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email should not be empty")
    private String email;
}
