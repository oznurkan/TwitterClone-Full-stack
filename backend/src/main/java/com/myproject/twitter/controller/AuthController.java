package com.myproject.twitter.controller;

import com.myproject.twitter.dto.request.RegisterRequestDto;
import com.myproject.twitter.dto.response.AuthResponseDto;
import com.myproject.twitter.dto.response.UserResponseDto;
import com.myproject.twitter.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto registerRequestDto){

        return authService.register(registerRequestDto);
    }

    @PostMapping("/login")
    public UserResponseDto login() {

        return authService.getUserByEmail();
    }

}
