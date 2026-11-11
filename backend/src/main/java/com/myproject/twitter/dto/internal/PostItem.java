package com.myproject.twitter.dto.internal;

import com.myproject.twitter.dto.response.UserProfilePostResponseDto;

import java.time.LocalDateTime;

public record PostItem(

        LocalDateTime createdAt,

        Long id,

        UserProfilePostResponseDto dto
) {
}
