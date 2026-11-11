package com.myproject.twitter.service;

import com.myproject.twitter.dto.request.RegisterRequestDto;
import com.myproject.twitter.dto.response.AuthResponseDto;
import com.myproject.twitter.dto.response.UserResponseDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto registerRequestDto);

    UserResponseDto getUserByEmail();

}
