package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record UserResponseDto(

        String nickName,

        String firstName,

        String lastName,

        String email,

        String bio,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
