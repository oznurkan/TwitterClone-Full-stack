package com.myproject.twitter.dto.response;

import java.time.LocalDateTime;

public record CommentResponseDto(

        Long commentId,
        Long tweetId,
        String firstName,
        String lastName,
        String nickName,
        String content,
        LocalDateTime updatedAt
) {
}
