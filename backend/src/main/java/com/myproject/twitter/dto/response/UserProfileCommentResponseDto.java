package com.myproject.twitter.dto.response;

public record UserProfileCommentResponseDto(

        String nickName,

        String text,

        TweetResponseDto tweet
) {
}
