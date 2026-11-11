package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record RetweetActionResponseDto(

        Long tweetId,

        String nickName,

        String text,

        LocalDateTime createdAt,

        Boolean isRetweetedByCurrentUser

) {
}
