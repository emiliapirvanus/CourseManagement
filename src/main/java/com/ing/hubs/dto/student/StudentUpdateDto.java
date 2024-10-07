package com.ing.hubs.dto.student;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class StudentUpdateDto {
    @Pattern(regexp = "((\\+\\d\\d?\\d?)|0)\\d{9}")
    private String phoneNumber;
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;
    private LocationDto location;
}
