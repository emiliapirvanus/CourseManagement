package com.ing.hubs.controller;


import com.ing.hubs.dto.JwtDto;
import com.ing.hubs.dto.StringResponseDto;
import com.ing.hubs.dto.UserResetPasswordDto;
import com.ing.hubs.dto.UserSessionDto;
import com.ing.hubs.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/sessions")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public JwtDto createSession(@RequestBody @Valid UserSessionDto dto) {
        return userService.createSession(dto);
    }

    @PatchMapping
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public StringResponseDto updatePassword(@Valid @RequestBody UserResetPasswordDto userResetPasswordDto) {
        return userService.updatePassword(userResetPasswordDto);
    }
}