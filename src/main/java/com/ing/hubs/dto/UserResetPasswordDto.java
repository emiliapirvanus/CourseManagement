package com.ing.hubs.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResetPasswordDto {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
