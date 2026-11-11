package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record UserProfilePostResponseDto(

        Long id,
        String type,
        String retweetText,
        TweetResponseDto tweet,
        LocalDateTime createdAt
) {
}
