package com.cts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPromotionDTO {

    @NotBlank(message = "Role cannot be empty")
    private String newRole;
}