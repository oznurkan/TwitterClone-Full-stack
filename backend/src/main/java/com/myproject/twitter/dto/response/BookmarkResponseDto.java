package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record BookmarkResponseDto(

        String nickName,

        Long tweetId,

        Boolean isBookmarkedByCurrentUser,

        LocalDateTime createdAt
) {
}
